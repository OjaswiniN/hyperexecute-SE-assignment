import org.testng.Assert;
import org.testng.annotations.Test;

public class ForceFailureTest {

    @Test(description = "Intentional failure used to verify HyperExecute retryOnFailure")
    public void intentionalFailureForRetry() {
        System.out.println("Intentional failure test executed. HyperExecute should retry this test.");
        Assert.fail("Intentional failure for Task 3 retry validation");
    }
}
