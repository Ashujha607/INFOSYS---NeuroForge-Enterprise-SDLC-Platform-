package com.neuroforge.repository;
import com.neuroforge.entity.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BugReportRepository extends JpaRepository<BugReport, Long> {}
