const API='http://localhost:8080/api';
const configs={
 users:{title:'Users',sub:'Manage users and roles',endpoint:'users',fields:[['name','Name','text'],['email','Email','email'],['password','Password','password'],['role','Role','select',['ADMIN','PROJECT_MANAGER','DEVELOPER','TESTER']]]},
 projects:{title:'Projects',sub:'Create and manage software projects',endpoint:'projects',fields:[['name','Project Name','text'],['description','Description','textarea'],['status','Status','select',['PLANNING','ACTIVE','COMPLETED']]]},
 sprints:{title:'Sprints',sub:'Plan iterations and track sprint status',endpoint:'sprints',fields:[['name','Sprint Name','text'],['goal','Sprint Goal','text'],['projectId','Project ID','projectId'],['status','Status','select',['PLANNED','ACTIVE','COMPLETED']]]},
 tasks:{title:'Tasks',sub:'Assign and track development tasks',endpoint:'tasks',fields:[['title','Task Title','text'],['description','Description','textarea'],['projectId','Project ID','projectId'],['assigneeId','Assignee ID','assigneeId'],['status','Status','select',['TODO','IN_PROGRESS','DONE']],[ 'priority','Priority','select',['LOW','MEDIUM','HIGH']]]},
 bugs:{title:'Bug Reports',sub:'Report and resolve defects',endpoint:'bugs',fields:[['title','Bug Title','text'],['description','Description','textarea'],['projectId','Project ID','projectId'],['reportedById','Reported By','reportedById'],['severity','Severity','select',['LOW','MEDIUM','HIGH','CRITICAL']],[ 'status','Status','select',['OPEN','IN_PROGRESS','RESOLVED','CLOSED']]]}
};
const page=document.getElementById('page');
function esc(v){return String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]))}

async function api(path,opt={}){
  const r=await fetch(API+path,{headers:{'Content-Type':'application/json'},...opt});
  if(!r.ok){
    const errT=await r.text();
    throw new Error(errT||r.statusText);
  }
  const txt=await r.text();
  return txt?JSON.parse(txt):null;
}

function toast(msg,bad=false){
  const t=document.getElementById('toast');
  if(!t) return;
  t.textContent=msg;
  t.className=bad?'bad':'show';
  setTimeout(()=>t.className='',3500);
}

async function fetchRefData(){
  let users=[], projects=[], sprints=[];
  try{ users=await api('/users'); }catch(e){}
  try{ projects=await api('/projects'); }catch(e){}
  try{ sprints=await api('/sprints'); }catch(e){}
  return { users, projects, sprints };
}

function formHTML(c, refData={}){
  const { users=[], projects=[], sprints=[] } = refData;
  return `<form class="form" id="entityForm">
    ${c.fields.map(f=>{
      const [name, label, type, opts] = f;
      if(type==='textarea') return `<textarea class="full" name="${name}" placeholder="${label}"></textarea>`;
      if(type==='select') return `<select name="${name}">${opts.map(x=>`<option value="${x}">${x}</option>`).join('')}</select>`;
      if(type==='projectId') return `<select name="${name}"><option value="">-- Project (Optional) --</option>${projects.map(p=>`<option value="${p.id}">${esc(p.name)}</option>`).join('')}</select>`;
      if(type==='assigneeId' || type==='reportedById') return `<select name="${name}"><option value="">-- User (Optional) --</option>${users.map(u=>`<option value="${u.id}">${esc(u.name)} (${esc(u.role)})</option>`).join('')}</select>`;
      return `<input name="${name}" type="${type}" placeholder="${label}">`;
    }).join('')}
    <div><button class="primary" type="submit" id="submitBtn">+ Add ${c.title.slice(0,-1)}</button></div>
  </form>`;
}

function renderTable(c,data){
  if(!data || !data.length) return '<div class="empty">No records found. Add your first record above.</div>';
  let keys=Object.keys(data[0]);
  return `<div class="table-wrap"><table><thead><tr>${keys.map(k=>`<th>${esc(k)}</th>`).join('')}<th>Action</th></tr></thead><tbody>${data.map(x=>`<tr>${keys.map(k=>{let val=x[k]; if(val && typeof val==='object'){val = val.name || val.title || val.id || JSON.stringify(val);} return `<td>${k==='password'?'••••••':esc(val)}</td>`}).join('')}<td><button class="danger" onclick="removeItem('${c.endpoint}',${x.id})">Delete</button></td></tr>`).join('')}</tbody></table></div>`;
}

async function loadEntity(key){
  const c=configs[key];
  document.getElementById('pageTitle').textContent=c.title;
  document.getElementById('pageSub').textContent=c.sub;
  page.innerHTML=`<div class="panel"><h2>Add ${c.title.slice(0,-1)}${c.title.endsWith('s')?'':''}</h2><div id="formContainer">Loading form...</div></div><div class="panel"><h2>All ${c.title}</h2><div id="tbl" class="empty">Loading...</div></div>`;
  const refData = await fetchRefData();
  document.getElementById('formContainer').innerHTML = formHTML(c, refData);
  
  const formEl = document.getElementById('entityForm');
  if(formEl){
    formEl.onsubmit = function(e){
      e.preventDefault();
      createItem(e, c.endpoint);
    };
  }
  
  try{
    const d=await api('/'+c.endpoint);
    document.getElementById('tbl').innerHTML=renderTable(c,d);
  }catch(e){
    document.getElementById('tbl').innerHTML='<div class="empty">Backend unavailable. Start Spring Boot on port 8080.</div>';
    toast('Backend unavailable',true);
  }
}

async function createItem(e,endpoint){
  if(e && e.preventDefault) e.preventDefault();
  const formEl = document.getElementById('entityForm') || (e ? e.target : null);
  if(!formEl) return;
  const obj=Object.fromEntries(new FormData(formEl).entries());
  
  if('projectId' in obj){
    if(obj.projectId && obj.projectId !== '') obj.project = { id: Number(obj.projectId) };
    delete obj.projectId;
  }
  if('assigneeId' in obj){
    if(obj.assigneeId && obj.assigneeId !== '') obj.assignedTo = { id: Number(obj.assigneeId) };
    delete obj.assigneeId;
  }
  if('reportedById' in obj){
    if(obj.reportedById && obj.reportedById !== '') obj.reportedBy = { id: Number(obj.reportedById) };
    delete obj.reportedById;
  }
  if('sprintId' in obj){
    if(obj.sprintId && obj.sprintId !== '') obj.sprint = { id: Number(obj.sprintId) };
    delete obj.sprintId;
  }

  try{
    await api('/'+endpoint, {method:'POST', body:JSON.stringify(obj)});
    toast('Saved successfully!');
    formEl.reset();
    const configKey = Object.keys(configs).find(k => configs[k].endpoint === endpoint);
    if(configKey) loadEntity(configKey);
  }catch(err){
    let msg = err.message || '';
    if(msg.includes('unique') || msg.includes('UK6dotkott2kjsp8vw4d0m25fb7') || msg.includes('ConstraintViolation')){
      msg = 'Email already exists! Please enter a unique email.';
    }
    toast('Save failed: ' + msg, true);
  }
}

async function removeItem(endpoint,id){
  if(!confirm('Delete this record?')) return;
  try{
    await api('/'+endpoint+'/'+id, {method:'DELETE'});
    toast('Deleted successfully!');
    const configKey = Object.keys(configs).find(k => configs[k].endpoint === endpoint);
    if(configKey) loadEntity(configKey);
  }catch(e){
    let msg = e.message || '';
    if(msg.includes('foreign key') || msg.includes('FK')){
      msg = 'Cannot delete: Record is linked to active tasks or bugs.';
    }
    toast('Delete failed: ' + msg, true);
  }
}

async function loadDashboard(){
  document.getElementById('pageTitle').textContent='Dashboard';
  document.getElementById('pageSub').textContent='NeuroForge SDLC overview';
  page.innerHTML='<div class="grid" id="stats"></div><div class="panel"><h2>Platform Modules</h2><div class="recent"><div class="item"><b>User & Role Management</b><span class="muted">Authentication, roles and access</span></div><div class="item"><b>Project & Team Management</b><span class="muted">Projects, teams and members</span></div><div class="item"><b>Sprint & Task Management</b><span class="muted">Iterations, assignments and progress</span></div><div class="item"><b>Testing, Bugs & Deployment</b><span class="muted">Defects, releases and monitoring</span></div></div></div>';
  const keys=Object.keys(configs);
  let html='';
  for(const k of keys){
    try{
      const d=await api('/'+configs[k].endpoint);
      html+=`<div class="card"><div class="label">${configs[k].title}</div><div class="num">${d.length}</div></div>`;
    }catch{
      html+=`<div class="card"><div class="label">${configs[k].title}</div><div class="num">—</div></div>`;
    }
  }
  document.getElementById('stats').innerHTML=html;
}

async function loadAiAssistant(){
  document.getElementById('pageTitle').textContent='AI Assistant';
  document.getElementById('pageSub').textContent='Powered by Groq GPT-OSS 20B Engine';
  page.innerHTML=`<div class="panel"><h2>🤖 Ask NeuroForge AI</h2><div style="display:flex;gap:10px;margin-bottom:15px;flex-wrap:wrap"><button class="primary" onclick="setPrompt('Explain the importance of fast language models')">⚡ Fast Language Models</button><button class="primary" style="background:#4b5563" onclick="setPrompt('Generate 5 sprint tasks for a User Authentication feature in Spring Boot')">📋 Generate Tasks</button><button class="primary" style="background:#dc2626" onclick="setPrompt('How to debug a memory leak in a Java Spring Boot application under heavy load?')">🐞 Debug Advice</button></div><form class="form" onsubmit="askAi(event)"><textarea id="aiPrompt" class="full" placeholder="Ask AI anything about your SDLC, architecture, code, or tasks..." required></textarea><div><button class="primary" id="aiBtn">🚀 Submit Query</button></div></form></div><div class="panel"><h2>AI Response</h2><div id="aiResponse" class="item" style="white-space:pre-wrap;line-height:1.6;font-family:inherit;min-height:100px;color:#334155">Click a quick prompt or type your query above to generate an AI response.</div></div>`;
}

function setPrompt(txt){ document.getElementById('aiPrompt').value=txt; }

async function askAi(e){
  e.preventDefault();
  const prompt=document.getElementById('aiPrompt').value;
  const respDiv=document.getElementById('aiResponse');
  const btn=document.getElementById('aiBtn');
  btn.disabled=true;
  btn.textContent='⏳ Thinking...';
  respDiv.innerHTML='<span style="color:#64748b">Querying Groq AI Model (openai/gpt-oss-20b)...</span>';
  try{
    const res=await api('/ai/chat',{method:'POST',body:JSON.stringify({prompt})});
    respDiv.textContent=res.response||'No response returned.';
    toast('AI Response received');
  }catch(err){
    respDiv.innerHTML='<span style="color:#ef4444">Error getting response: '+esc(err.message)+'</span>';
    toast('AI request failed',true);
  }finally{
    btn.disabled=false;
    btn.textContent='🚀 Submit Query';
  }
}

window.createItem = createItem;
window.removeItem = removeItem;
window.loadEntity = loadEntity;
window.setPrompt = setPrompt;
window.askAi = askAi;
window.loadDashboard = loadDashboard;

document.querySelectorAll('.nav').forEach(b=>b.onclick=()=>{
  document.querySelectorAll('.nav').forEach(x=>x.classList.remove('active'));
  b.classList.add('active');
  if(b.dataset.page==='dashboard') loadDashboard();
  else if(b.dataset.page==='ai') loadAiAssistant();
  else loadEntity(b.dataset.page);
});

loadDashboard();
api('/users').then(()=>document.getElementById('status').textContent='Online').catch(()=>document.getElementById('status').textContent='Offline');
