package com.project.CustomerProject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/batch")
public class BatchController {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    private Job job;

    @GetMapping(value = "/set-job")
    public String setJob(Model model) {
        BatchJob batchJob = new BatchJob();
        model.addAttribute("batchJob", batchJob);
        return "set-batch-job";
    }

    @PostMapping(value = "/job")
    public String testJob(@ModelAttribute(name = "batchJob") BatchJob batchJob) throws NoSuchJobException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        String jobId = batchJob.id.toString();
        if (StringUtils.hasLength(jobId)) {
            jobParametersBuilder.addString("jobId", jobId);
        }
        JobExecution jobExecution;
        try {
            jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        try {
            Set<Long> executions = jobOperator.getRunningExecutions("book-loader-job");
            jobOperator.stop(executions.iterator().next());
        }catch(Exception e){
            System.out.println("Error stopping job.");
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
}
