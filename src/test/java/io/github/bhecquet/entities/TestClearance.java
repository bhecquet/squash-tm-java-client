package io.github.bhecquet.entities;

import io.github.bhecquet.exceptions.NotImplementedException;
import org.testng.annotations.Test;

public class TestClearance {

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        Clearance cl = new Clearance("osef", "ouais", 1, "yolo");
        cl.completeDetails();
    }

}
