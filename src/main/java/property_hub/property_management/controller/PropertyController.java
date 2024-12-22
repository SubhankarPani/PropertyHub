package property_hub.property_management.controller;

import property_hub.property_management.model.Property; // Correct import for the Property entity
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import property_hub.property_management.repository.PropertyRepository;

import java.util.List;

@Controller
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepo;

    // Show the property form for adding new properties
    @GetMapping("/property-form")
    public String showPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        return "propertyForm";
    }

    // Submit the property details to save it to the database
    @PostMapping("/submit-property")
    public String submitProperty(@ModelAttribute Property property, Model model) {
        // Assuming the seller's email is available in the session or provided during form submission
        String sellerEmail = property.getOwnerEmail(); // Replace with actual logged-in user's email
        property.setOwnerEmail(sellerEmail);

        // Log the property email to verify it's set correctly
        System.out.println("Submitting property for seller: " + sellerEmail);

        // Save the property to the database
        propertyRepo.save(property);

        // Redirect to a thank you page with a message
        model.addAttribute("message", "Property details submitted successfully!");
        return "thankyou";
    }

    // Show the list of properties for the seller
    @GetMapping("/seller-property-list")
    public String showSellerPropertyList(Model model) {
        // Fetch logged-in seller's email (for simplicity, hardcoded here)
        String sellerEmail = "seller@example.com"; // Replace with actual logged-in user's email

        // Get properties for the logged-in seller only
        List<Property> properties = propertyRepo.findByOwnerEmail(sellerEmail);

        // Log the list of properties to check if they are fetched
        System.out.println("Fetched properties for seller " + sellerEmail + ": " + properties.size() + " properties found.");

        model.addAttribute("properties", properties);
        return "sellerPropertyList";
    }

    // Show the form for modifying a specific property
    @GetMapping("/modify-property/{id}")
    public String showModifyForm(@PathVariable("id") Integer id, Model model) {
        // Fetch the property by its ID
        Property property = propertyRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid property ID"));

        model.addAttribute("property", property);
        return "propertyForm";  // Same form for adding and modifying properties
    }

    // Update an existing property
    @PostMapping("/update-property")
    public String updateProperty(@ModelAttribute Property property, Model model) {
        // Assuming the seller's email is available in the session or provided during form submission
        String sellerEmail = "seller@example.com"; // Replace with actual logged-in user's email
        property.setOwnerEmail(sellerEmail);

        // Save or update the property in the database
        propertyRepo.save(property);

        model.addAttribute("message", "Property details updated successfully!");
        return "thankyou"; // Redirect to a thank you page after update
    }

    // Delete a property
    @PostMapping("/delete-property/{id}")
    public String deleteProperty(@PathVariable("id") Integer id, Model model) {
        // Fetch the property by its ID
        Property property = propertyRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid property ID"));

        // Delete the property from the database
        propertyRepo.delete(property);

        // Redirect to the list of properties with a success message
        model.addAttribute("message", "Property deleted successfully!");
        return "redirect:/seller-property-list"; // Redirect to the property list page after deletion
    }
}
