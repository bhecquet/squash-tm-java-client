package io.github.bhecquet.entities;

import io.github.bhecquet.SquashTMTest;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class TestEntityCache extends SquashTMTest {

    @Mock
    static Requirement requirement1;

    @Mock
    static Requirement requirement2;

    @Mock
    Project project;

    @BeforeMethod
    public void setUp() {
        when(requirement1.getId()).thenReturn(1);
        when(requirement2.getId()).thenReturn(2);
    }

    @Test
    public void testUseCache() {

        try (MockedStatic<Requirement> mockedRequirement = Mockito.mockStatic(Requirement.class, Mockito.CALLS_REAL_METHODS)) {

            mockedRequirement.when(() -> Requirement.getAll(project, "path,name,reference")).thenReturn(List.of(requirement1, requirement2));

            EntityCache.setEnabled(true);

            EntityCache<Requirement> cache = new EntityCache<>(10);
            List<Requirement> requirements = cache.getAll(Requirement::getAll, project);
            Assert.assertEquals(requirements.size(), 2);
            Assert.assertEquals(requirements.get(0), requirement1);
            Assert.assertEquals(requirements.get(1), requirement2);

            // call a second time, we should not reach Squash and get cache
            requirements = cache.getAll(Requirement::getAll, project);
            Assert.assertEquals(requirements.size(), 2);

            mockedRequirement.verify(() -> Requirement.getAll(project, "path,name,reference"));
        }
    }

    @Test
    public void testCacheDisabled() {

        try (MockedStatic<Requirement> mockedRequirement = Mockito.mockStatic(Requirement.class, Mockito.CALLS_REAL_METHODS)) {

            mockedRequirement.when(() -> Requirement.getAll(project, "path,name,reference")).thenReturn(List.of(requirement1, requirement2));

            EntityCache.setEnabled(false);

            EntityCache<Requirement> cache = new EntityCache<>(10);
            List<Requirement> requirements = cache.getAll(Requirement::getAll, project);
            Assert.assertEquals(requirements.size(), 2);
            Assert.assertEquals(requirements.get(0), requirement1);
            Assert.assertEquals(requirements.get(1), requirement2);

            // call a second time, we should not reach Squash and get cache
            requirements = cache.getAll(Requirement::getAll, project);
            Assert.assertEquals(requirements.size(), 2);

            mockedRequirement.verify(() -> Requirement.getAll(project, "path,name,reference"), times(2));
        }

    }

}
