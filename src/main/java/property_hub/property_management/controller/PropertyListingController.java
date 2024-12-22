package property_hub.property_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import property_hub.property_management.model.Property;
import property_hub.property_management.repository.PropertyRepository;

import java.util.List;

@Controller
public class PropertyListingController {

    @Autowired
    private PropertyRepository propertyRepo;

    @GetMapping("/property-listing")
    public String propertyListingPage(Model model) {
        // Fetch all properties from the database
        List<Property> properties = propertyRepo.findAll();
        model.addAttribute("properties", properties); // Add properties to the model
        return "propertyListing";  // Return to the property listing page view
    }
}
