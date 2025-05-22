package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Integer> {
    
    // Find by resource type (Article, Video, Guide)
    List<Resources> findByResourceType(String resourceType);

    // Find by tag (Trending, Best Practices)
    List<Resources> findByTag(String tag);

    // Find by both resource type and tag
    List<Resources> findByResourceTypeAndTag(String resourceType, String tag);
}
