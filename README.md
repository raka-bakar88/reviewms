This is an Review ms application that is part of Distributed/Microservices architecture that provides a functionality to review a company based on a job. The containeraized versions of this applications is available in [my docker hub account ](https://hub.docker.com/u/rkabkr). It uses Docker and Kubernetes to create and maintain the Images, Pods etc. 

**Techstack**
- Spring Boot(Java 17)
- PostgreSQL database
- RabbitMQ
- Spring Eureka
- Kubernetes
- Spring Config Server
- Zipkin
- Spring Actuator
- OpenFeign
- Java Persistence Api (JPA)
- Lombok
- Model Mapper
- Maven

  **Architecture**

  ![alt text](https://github.com/raka-bakar88/jobms/blob/main/microservice%20architecture%20diagram.png)

This Job microservice works together with Company and Review Microservice application. Each Microservice has its own PostgresSQL database. The communication between Microservices is handled by Queues Messages tool using RabbitMQ. Configuration data for each Microservice is supported by Spring Config Server, so before each Microservice is run, they will fetch the config data from config server. Spring Cloud Eureka Server is used to discover, register and maintain status of all Microservices applications. Zipkin is also used to monitor the performance of all Microservices.

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
