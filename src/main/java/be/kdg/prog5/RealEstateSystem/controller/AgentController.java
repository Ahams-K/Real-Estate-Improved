package be.kdg.prog5.RealEstateSystem.controller;

import be.kdg.prog5.RealEstateSystem.controller.viewmodel.AddAgentForm;
import be.kdg.prog5.RealEstateSystem.service.AgentServiceInterface;
import be.kdg.prog5.RealEstateSystem.service.PropertyServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/agents")
public class AgentController {

    private final AgentServiceInterface agentService;
    private final PropertyServiceInterface propertyService;

    public AgentController(AgentServiceInterface agentService, PropertyServiceInterface propertyService) {
        this.agentService = agentService;
        this.propertyService = propertyService;
    }

    @GetMapping
    public ModelAndView showAgents() {
        ModelAndView view = new ModelAndView("agents");
        view.addObject("Agents", agentService.getALL());
        return view;
    }

    @GetMapping("/{agentId}")
    public ModelAndView viewAgentDetails(@PathVariable UUID agentId) {
        final ModelAndView view = new ModelAndView("agent-details");
        view.addObject("agent", agentService.findAgentById(agentId));
        return view;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addAgent(){
        final ModelAndView view = new ModelAndView("addagent");
        final AddAgentForm form = new AddAgentForm();
        view.addObject("properties", propertyService.getAll());
        view.addObject("form", form);
        return view;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@ModelAttribute final AddAgentForm form){
        agentService.add(form.getAgentName(), form.getPassword(), form.getContactInfo(), form.getLicenceNumber(), form.getEmail(), form.getAgentProperties());
        return "redirect:/agents";
    }

    @GetMapping("/update/{agentId}")
    public ModelAndView update(@PathVariable final UUID agentId){
        ModelAndView view = new ModelAndView("update-agent");
        view.addObject("agent", agentService.findAgentById(agentId));
        return view;
    }
}
