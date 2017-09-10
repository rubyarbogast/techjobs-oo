package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        Job job = jobData.findById(id);

        model.addAttribute("job", job);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors, @RequestParam String name) {

        if (errors.hasErrors()){
            model.addAttribute("job", jobForm);
            return "new-job";
        }

        Employer newEmployer = jobData.getEmployers().findById(jobForm.getEmployerId());
        Location newLocation = jobData.getLocations().findById(jobForm.getLocationId());
        PositionType newPositionType = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
        CoreCompetency newCoreCompetency = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());

        Job job = new Job(name, newEmployer, newLocation, newPositionType, newCoreCompetency);

        jobData.add(job);

        model.addAttribute("job", job);

        return "redirect:/job?id=" + job.getId();

        //return "job-detail";
    }
}
