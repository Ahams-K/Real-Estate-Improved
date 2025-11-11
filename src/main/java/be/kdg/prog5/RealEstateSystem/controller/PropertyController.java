package be.kdg.prog5.RealEstateSystem.controller;

import be.kdg.prog5.RealEstateSystem.controller.viewmodel.PropertiesViewModel;
import be.kdg.prog5.RealEstateSystem.controller.viewmodel.PropertiesWithAssignedAgentsViewModel;
import be.kdg.prog5.RealEstateSystem.controller.viewmodel.PropertyViewModel;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import be.kdg.prog5.RealEstateSystem.service.AuthorizationService;
import be.kdg.prog5.RealEstateSystem.service.PropertyServiceInterface;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/properties")
public class PropertyController {
    private final PropertyServiceInterface propertyService;
    private final AuthorizationService authorizationService;

    public PropertyController(PropertyServiceInterface propertyService, AuthorizationService authorizationService) {
        this.propertyService = propertyService;
        this.authorizationService = authorizationService;
    }

    @GetMapping
    public ModelAndView showProperties(@AuthenticationPrincipal final CustomUserDetails userDetails
                                      ){
        ModelAndView view = new ModelAndView("properties");
        view.addObject("Properties", new PropertiesViewModel(
                propertyService.getAll()
                        .stream()
                        .map(property -> PropertiesWithAssignedAgentsViewModel.from(
                                property,
                                authorizationService.isModificationAllowed(userDetails, property)
                        )).toList()
        ));
        return view;
    }


    @GetMapping("/{propertyId}")
    public ModelAndView showPropertyDetails(@PathVariable UUID propertyId,
                                            @AuthenticationPrincipal final CustomUserDetails userDetails){
        final ModelAndView view = new ModelAndView("property-details");
        view.addObject("property",
               PropertyViewModel.from(propertyService.findPropertyById(propertyId),
                       authorizationService.isModificationAllowed(userDetails, propertyId)));
        return view;
    }

    @GetMapping("/add")
    public ModelAndView addProperty(){
        return new ModelAndView("addproperty");
    }

    @PostMapping("/filter")
    public String filterProperties(
            Double minPrice,
            Double maxPrice,
            PropertyType type,
            PropertyStatus status,
            Model model) {
        model.addAttribute("Properties", new PropertiesViewModel(
                propertyService.getFilteredProperties(minPrice, maxPrice, type, status)
                        .stream()
                        .map(property -> PropertiesWithAssignedAgentsViewModel.from(property, false)) // No modification permission needed
                        .toList()
        ));
        return "properties";
    }
}
