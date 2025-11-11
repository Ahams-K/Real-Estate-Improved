package be.kdg.prog5.RealEstateSystem.controller;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.service.AgencyServiceInterface;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/agencies")
public class AgencyController{

    private final AgencyServiceInterface agencyService;

    public AgencyController(AgencyServiceInterface agencyService) {
        this.agencyService = agencyService;
    }

    @GetMapping
    public ModelAndView showAgency(){
        ModelAndView view = new ModelAndView("agencies");
        view.addObject("Agencies", agencyService.getAll());
        return view;
    }

    @GetMapping("/{agencyId}")
    public ModelAndView viewAgencyDetails(@PathVariable UUID agencyId){
        ModelAndView view = new ModelAndView("agency-details");
        try {
            RealEstateAgency agency = agencyService.findAgencyById(agencyId).orElseThrow(() -> NotFoundException.forAgent(agencyId));
            view.addObject("agency", agency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return view;
    }
}
