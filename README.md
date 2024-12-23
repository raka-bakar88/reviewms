This is an Review ms application that is part of Distributed/Microservices architecture that provides a functionality to review a company based on a job. The containerized  versions of this applications is available in [my docker hub account ](https://hub.docker.com/u/rkabkr). It employs Docker and Kubernetes for creating and managing images, pods, and other resources.

**Techstack**
- Backend Framework: Spring Boot (Java 17)
- Database: PostgreSQL
- Messaging System: RabbitMQ
- Service Discovery: Spring Eureka
- Orchestration & Scaling: Kubernetes
- Configuration Management: Spring Config Server
- Distributed Tracing: Zipkin
- Health Monitoring: Spring Actuator
- Service-to-Service Communication: OpenFeign
- ORM Framework: Java Persistence API (JPA)
- Utilities: Lombok, Model Mapper
- Build Tool: Maven

  **Architecture**

  ![alt text](https://github.com/raka-bakar88/jobms/blob/main/microservice%20architecture%20diagram.png)

This application follows a Microservices Architecture where each service operates independently with its own database and interacts with others through messaging and service discovery mechanisms.

Microservices Overview:

1. Review Microservice works in conjunction with Company and Job microservices.

2. Each microservice uses its own PostgreSQL database.

3. Communication between microservices is handled through RabbitMQ message queues.

4. Configuration data for each microservice is managed by the Spring Config Server. Before launching, each service fetches its configuration settings.

5. Spring Cloud Eureka Server is used for service discovery, registration, and health monitoring.

6. Zipkin monitors and traces the performance of all microservices.

**Database**

Each of the Microservice application uses its own PostgresSQL as the database. However, they are connected to each other. below is the Entity Relationship Diagram of the database
![alt text](https://github.com/raka-bakar88/jobms/blob/main/JobApp%20ER%20Diagram.png)

**JOB Endpoints**
![alt text](https://github.com/raka-bakar88/reviewms/blob/main/review%20microservice%20api%20list.png)

**Example Class**


  Below is an example of code from Class ReviewServiceImpl
   ````
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<ReviewDTO> getAllReviews(Long companyId) {
        List<Review> reviews = reviewRepository.findReviewsByCompanyId(companyId);

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class)).toList();
    }

    @Override
    public Boolean createReview(Long companyId, ReviewDTO reviewDTO) {
        if (companyId != null && reviewDTO != null) {
            Review review = modelMapper.map(reviewDTO, Review.class);
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "ReviewId", reviewId));

        return modelMapper.map(review, ReviewDTO.class);
    }

    @Override
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository
                .findById(reviewId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review", "ReviewId", reviewId));
        review.setDescription(reviewDTO.getDescription());
        review.setTitle(reviewDTO.getTitle());
        review.setRating(reviewDTO.getRating());

        Review updatedReview = reviewRepository.save(review);
        return modelMapper.map(updatedReview, ReviewDTO.class);
    }

    @Override
    public String deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new ResourceNotFoundException("Review", "Review ID", reviewId));
        reviewRepository.deleteById(reviewId);
        return "Delete successfull";
    }
}
   ````


Link to other Microservices repository:
- [Companyms](https://github.com/raka-bakar88/companyms)
- [jobms](https://github.com/raka-bakar88/jobms)
- [Eureka Service Registration Server ms](https://github.com/raka-bakar88/eurekams)
- [Config Server ms](https://github.com/raka-bakar88/configserverms)
- [Api gateway ms](https://github.com/raka-bakar88/gatewayms)
