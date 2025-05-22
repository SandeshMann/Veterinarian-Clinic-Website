package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Resources;
import au.edu.rmit.sept.webapp.repositories.ResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourcesService {

    // ResourcesRepository instance to interact with the database
    @Autowired
    private ResourcesRepository resourcesRepository;

    // Method to get resources by their type
    public List<Resources> getResourcesByType(String resourceType) {
        return resourcesRepository.findByResourceType(resourceType);
    }

    // Method to get resources by their tag
    public List<Resources> getResourcesByTag(String tag) {
        return resourcesRepository.findByTag(tag);
    }

    // Method to get resources by both type and tag
    public List<Resources> getResourcesByTypeAndTag(String resourceType, String tag) {
        return resourcesRepository.findByResourceTypeAndTag(resourceType, tag);
    }

    // Method to get all resources
    public List<Resources> getAllResources() {
        return resourcesRepository.findAll();
    }
}
