import com.minecraft.job.common.review.domain.Review;

public interface ReviewService {

    Review create(Long userId, Long teamId, String content, Long score);
}
