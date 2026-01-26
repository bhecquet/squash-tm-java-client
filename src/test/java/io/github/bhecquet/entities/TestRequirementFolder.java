package io.github.bhecquet.entities;


import io.github.bhecquet.SquashTMTest;
import io.github.bhecquet.exceptions.NotImplementedException;
import io.github.bhecquet.exceptions.SquashTmException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class TestRequirementFolder extends SquashTMTest {

    public static final String SIMPLE_GET_REQ_FOLDER = "{\n" +
            "  \"_type\" : \"requirement-folder\",\n" +
            "  \"id\" : 356,\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 14,\n" +
            "    \"name\" : \"Test Project 1\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/12\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"path\" : \"/sample project/sample parent folder/embedded folder\",\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 34,\n" +
            "    \"name\" : \"sample parent folder\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-folders/34\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"custom_fields\" : [ ],\n" +
            "  \"name\" : \"embedded folder\",\n" +
            "  \"created_by\" : \"User-1\",\n" +
            "  \"created_on\" : \"2017-07-19T10:00:00.000+00:00\",\n" +
            "  \"last_modified_by\" : \"User-2\",\n" +
            "  \"last_modified_on\" : \"2017-07-20T10:00:00.000+00:00\",\n" +
            "  \"description\" : \"<p>An embedded folder...</p>\",\n" +
            "  \"attachments\" : [ ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/12\"\n" +
            "    },\n" +
            "    \"content\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356/content\"\n" +
            "    },\n" +
            "    \"attachments\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356/attachments\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String SIMPLE_GET_REQ_FOLDER_TOP_LEVEL = "{\n" +
            "  \"_type\" : \"requirement-folder\",\n" +
            "  \"id\" : 356,\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 12,\n" +
            "    \"name\" : \"sample project\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/12\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"path\" : \"/sample project/embedded folder\",\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 14,\n" +
            "    \"name\" : \"sample project\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/14\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"custom_fields\" : [ ],\n" +
            "  \"name\" : \"embedded folder\",\n" +
            "  \"created_by\" : \"User-1\",\n" +
            "  \"created_on\" : \"2017-07-19T10:00:00.000+00:00\",\n" +
            "  \"last_modified_by\" : \"User-2\",\n" +
            "  \"last_modified_on\" : \"2017-07-20T10:00:00.000+00:00\",\n" +
            "  \"description\" : \"<p>An embedded folder...</p>\",\n" +
            "  \"attachments\" : [ ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/12\"\n" +
            "    },\n" +
            "    \"content\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356/content\"\n" +
            "    },\n" +
            "    \"attachments\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/356/attachments\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String REQ_FOLDER_TREE_PROJECT = "[ {\n" +
            "  \"_type\" : \"project\",\n" +
            "  \"id\" : 10,\n" +
            "  \"name\" : \"project-1\",\n" +
            "  \"folders\" : [ {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 100,\n" +
            "    \"name\" : \"folder1\",\n" +
            "    \"url\" : \"https://localhost:4321/requirement-folders/100\",\n" +
            "    \"children\" : [{\n" +
            "                \"_type\": \"requirement-folder\",\n" +
            "                \"id\": 101,\n" +
            "                \"name\": \"folder11\",\n" +
            "                \"url\": \"https://localhost:4321/requirement-folders/101\",\n" +
            "                \"children\": []\n" +
            "              }]\n" +
            "  }, {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 110,\n" +
            "    \"name\" : \"folder2\",\n" +
            "    \"url\" : \"https://localhost:4321/requirement-folders/110\",\n" +
            "    \"children\" : [ ]\n" +
            "  } ]\n" +
            "}]";

    public static final String REQ_FOLDER_CONTENT = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"content\": [\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 101,\n" +
            "        \"name\": \"req1\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/101\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 102,\n" +
            "        \"name\": \"req2\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/102\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 103,\n" +
            "        \"name\": \"req3\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/103\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 104,\n" +
            "        \"name\": \"req4\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/104\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 105,\n" +
            "        \"name\": \"req5\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/105\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement\",\n" +
            "        \"id\": 106,\n" +
            "        \"name\": \"req6\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirements/106\"\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"_type\": \"requirement-folder\",\n" +
            "        \"id\": 100,\n" +
            "        \"name\": \"folder1\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"https://localhost:4321/requirement-folders/100\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://localhost:4321/requirement-folders/1010/content?page=0&size=20\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 20,\n" +
            "    \"totalElements\": 7,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    public static final String REQ_FOLDER_ALL = "{\n" +
            "  \"_embedded\" : {\n" +
            "    \"requirement-folders\" : [ {\n" +
            "      \"_type\" : \"requirement-folder\",\n" +
            "      \"id\" : 23,\n" +
            "      \"name\" : \"sample folder 1\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/requirement-folders/23\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"requirement-folder\",\n" +
            "      \"id\" : 26,\n" +
            "      \"name\" : \"sample folder 2\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/requirement-folders/26\"\n" +
            "        }\n" +
            "      }\n" +
            "    }, {\n" +
            "      \"_type\" : \"requirement-folder\",\n" +
            "      \"id\" : 31,\n" +
            "      \"name\" : \"sample folder 3\",\n" +
            "      \"_links\" : {\n" +
            "        \"self\" : {\n" +
            "          \"href\" : \"https://localhost:4321/requirement-folders/31\"\n" +
            "        }\n" +
            "      }\n" +
            "    } ]\n" +
            "  },\n" +
            "  \"_links\" : {\n" +
            "    \"first\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders?page=0&size=3\"\n" +
            "    },\n" +
            "    \"prev\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders?page=0&size=3\"\n" +
            "    },\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders?page=1&size=3\"\n" +
            "    },\n" +
            "    \"next\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders?page=2&size=3\"\n" +
            "    },\n" +
            "    \"last\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders?page=3&size=3\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\" : {\n" +
            "    \"size\" : 3,\n" +
            "    \"totalElements\" : 10,\n" +
            "    \"totalPages\" : 4,\n" +
            "    \"number\" : 1\n" +
            "  }\n" +
            "}";

    public static final String REQ_FOLDER_CREATION_RESPONSE = "{\n" +
            "  \"_type\" : \"requirement-folder\",\n" +
            "  \"id\" : 33,\n" +
            "  \"project\" : {\n" +
            "    \"_type\" : \"project\",\n" +
            "    \"id\" : 14,\n" +
            "    \"name\" : \"Test Project 1\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/projects/14\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"path\" : \"/Folder 1/Requirement folder 1\",\n" +
            "  \"parent\" : {\n" +
            "    \"_type\" : \"requirement-folder\",\n" +
            "    \"id\" : 11,\n" +
            "    \"name\" : \"Test folder not null motherfocker\",\n" +
            "    \"_links\" : {\n" +
            "      \"self\" : {\n" +
            "        \"href\" : \"https://localhost:4321/requirement-folders/11\"\n" +
            "      }\n" +
            "    }\n" +
            "  },\n" +
            "  \"custom_fields\" : [ {\n" +
            "    \"code\" : \"cuf1\",\n" +
            "    \"label\" : \"Lib Cuf1\",\n" +
            "    \"value\" : \"Cuf1 Value\"\n" +
            "  }, {\n" +
            "    \"code\" : \"cuf2\",\n" +
            "    \"label\" : \"Lib Cuf2\",\n" +
            "    \"value\" : \"true\"\n" +
            "  } ],\n" +
            "  \"name\" : \"Requirement folder 1\",\n" +
            "  \"created_by\" : \"admin\",\n" +
            "  \"created_on\" : \"2017-07-19T10:00:00.000+00:00\",\n" +
            "  \"last_modified_by\" : \"admin\",\n" +
            "  \"last_modified_on\" : \"2017-07-19T10:00:00.000+00:00\",\n" +
            "  \"description\" : null,\n" +
            "  \"attachments\" : [ ],\n" +
            "  \"_links\" : {\n" +
            "    \"self\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/33\"\n" +
            "    },\n" +
            "    \"project\" : {\n" +
            "      \"href\" : \"https://localhost:4321/projects/14\"\n" +
            "    },\n" +
            "    \"content\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/33/content\"\n" +
            "    },\n" +
            "    \"attachments\" : {\n" +
            "      \"href\" : \"https://localhost:4321/requirement-folders/33/attachments\"\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String REQ_FOLDER_100_REPLY_DATA = "{"
            + "  \"_type\" : \"requirement-folder\","
            + "  \"id\" : 100,"
            + "  \"name\" : \"folder1\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder1\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old requirements go</p>\","
            + "  \"custom_fields\" : [ {"
            + "    \"code\" : \"CF_TXT\","
            + "    \"label\" : \"test level\","
            + "    \"value\" : \"mandatory\""
            + "  }, {"
            + "    \"code\" : \"CF_TAGS\","
            + "    \"label\" : \"see also\","
            + "    \"value\" : [ \"walking\", \"bipedal\" ]"
            + "  } ],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/100\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/100/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/100/attachments\""
            + "    }"
            + "  }"
            + "}";
    public static final String REQ_FOLDER_101_REPLY_DATA = "{"
            + "  \"_type\" : \"requirement-folder\","
            + "  \"id\" : 101,"
            + "  \"name\" : \"folder2\","
            + "  \"project\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"path\" : \"/Mangrove/folder2\","
            + "  \"parent\" : {"
            + "    \"_type\" : \"project\","
            + "    \"id\" : 14,"
            + "    \"name\" : \"Mangrove\","
            + "    \"_links\" : {"
            + "      \"self\" : {"
            + "        \"href\" : \"https://localhost:4321/projects/14\""
            + "      }"
            + "    }"
            + "  },"
            + "  \"created_by\" : \"User-1\","
            + "  \"created_on\" : \"2011-09-30T10:00:00.000+00:00\","
            + "  \"last_modified_by\" : \"admin\","
            + "  \"last_modified_on\" : \"2017-06-16T10:00:00.000+00:00\","
            + "  \"description\" : \"<p>where all the old requirements go</p>\","
            + "  \"custom_fields\" : [ {"
            + "    \"code\" : \"CF_TXT\","
            + "    \"label\" : \"test level\","
            + "    \"value\" : \"mandatory\""
            + "  }, {"
            + "    \"code\" : \"CF_TAGS\","
            + "    \"label\" : \"see also\","
            + "    \"value\" : [ \"walking\", \"bipedal\" ]"
            + "  } ],"
            + "  \"attachments\" : [ ],"
            + "  \"_links\" : {"
            + "    \"self\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/101\""
            + "    },"
            + "    \"project\" : {"
            + "      \"href\" : \"https://localhost:4321/projects/14\""
            + "    },"
            + "    \"content\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/101/content\""
            + "    },"
            + "    \"attachments\" : {"
            + "      \"href\" : \"https://localhost:4321/requirement-folders/101/attachments\""
            + "    }"
            + "  }"
            + "}";

    public static final String REQ_FOLDER_SUBFOLDER_RESPONSE = "[ {"
            + "  \"_type\" : \"project\","
            + "  \"id\" : 10,"
            + "  \"name\" : \"project-1\","
            + "  \"folders\" : [ {"
            + "    \"_type\" : \"requirement-folder\","
            + "    \"id\" : 100,"
            + "    \"name\" : \"folder1\","
            + "    \"url\" : \"https://localhost:4321/requirement-folders/100\","
            + "    \"children\" : [ {"
            + "      \"_type\" : \"requirement-folder\","
            + "      \"id\" : 1000,"
            + "      \"name\" : \"sub-folder1\","
            + "      \"url\" : \"https://localhost:4321/requirement-folders/19519\","
            + "      \"children\" : [ {"
            + "        \"_type\" : \"requirement-folder\","
            + "        \"id\" : 10000,"
            + "        \"name\" : \"sub-sub-folder1\","
            + "        \"url\" : \"https://localhost:4321/requirement-folders/19880\","
            + "        \"children\" : [ ]"
            + "      }, {"
            + "        \"_type\" : \"requirement-folder\","
            + "        \"id\" : 10001,"
            + "        \"name\" : \"sub-sub-folder2\","
            + "        \"url\" : \"https://localhost:4321/requirement-folders/19881\","
            + "        \"children\" : [ ]"
            + "      } ]"
            + "    } ]"
            + "  }, {"
            + "    \"_type\" : \"requirement-folder\","
            + "    \"id\" : 101,"
            + "    \"name\" : \"folder2\","
            + "    \"url\" : \"https://localhost:4321/requirement-folders/101\","
            + "    \"children\" : [ ]"
            + "  } ]"
            + "}]";

    private Project project;

    @BeforeMethod
    public void init() {
        project = new Project("https://localhost:4321/projects/14", "project", 14, "Test Project 1");
        Campaign.configureEntity("user", "pwd", SERVER_URL + "/");
    }

    @Test
    public void testGetAll() {
        createServerMock("GET", "/requirement-folders", 200, REQ_FOLDER_ALL);
        List<RequirementFolder> requirementFolders = RequirementFolder.getAll();
        Assert.assertEquals(requirementFolders.size(), 3);
        Assert.assertEquals(requirementFolders.get(0).getName(), "sample folder 1");
        Assert.assertEquals(requirementFolders.get(0).getId(), 23);
        Assert.assertEquals(requirementFolders.get(0).getUrl(), "https://localhost:4321/requirement-folders/23");
    }

    @Test
    public void testGetAllByProject() {

        createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        List<RequirementFolder> requirementFolders = RequirementFolder.getAll(project);
        Assert.assertEquals(requirementFolders.size(), 3);
        Assert.assertEquals(requirementFolders.get(0).getName(), "folder1");
        Assert.assertEquals(requirementFolders.get(0).getId(), 100);
        Assert.assertEquals(requirementFolders.get(0).getUrl(), "https://localhost:4321/requirement-folders/100");
        Assert.assertEquals(requirementFolders.get(0).getProject(), project);
        Assert.assertNull(requirementFolders.get(0).getParent());
        Assert.assertEquals(requirementFolders.get(1).getId(), 101);
        Assert.assertEquals(requirementFolders.get(1).getParent(), requirementFolders.get(0));
        Assert.assertEquals(requirementFolders.get(1).getProject(), project);
        Assert.assertEquals(requirementFolders.get(2).getId(), 110);
    }

    @Test
    public void testAsJson() {
        RequirementFolder requirementFolder = new RequirementFolder(
                "https://localhost:4321/requirement-folders/7",
                "requirement-folder",
                7,
                "folder",
                project,
                null);
        JSONObject json = requirementFolder.asJson();
        Assert.assertEquals(json.getInt("id"), 7);
        Assert.assertEquals(json.getString("name"), "folder");
        Assert.assertEquals(json.getString("_type"), "requirement-folder");
    }

    @Test
    public void testFromJsonNoParent() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-folder/1\"" +
                "        }}"));

        RequirementFolder requirementFolder = RequirementFolder.fromJson(json);
        Assert.assertEquals(requirementFolder.getId(), 1);
        Assert.assertEquals(requirementFolder.getName(), "foo");
        Assert.assertEquals(requirementFolder.getUrl(), "https://localhost:4321/requirement-folder/1");
        Assert.assertNull(requirementFolder.getParent());
        Assert.assertNull(requirementFolder.getProject());
    }

    @Test(expectedExceptions = SquashTmException.class)
    public void testFromJsonWrongFormat() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-folder");
        json.put("id", 1);
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-folder/1\"" +
                "        }}"));

        RequirementFolder.fromJson(json);
    }

    @Test
    public void testFromJsonWithProject() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-folder/1\"" +
                "        }}"));
        json.put("project", new JSONObject("{" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/project/10\"" +
                "      }" +
                "    }}"));

        RequirementFolder requirementFolder = RequirementFolder.fromJson(json);
        Assert.assertEquals(requirementFolder.getId(), 1);
        Assert.assertEquals(requirementFolder.getName(), "foo");
        Assert.assertEquals(requirementFolder.getUrl(), "https://localhost:4321/requirement-folder/1");
        Assert.assertTrue(requirementFolder.getProject() instanceof Project);
        Assert.assertEquals(requirementFolder.getProject().getId(), 10);
    }

    @Test
    public void testFromJsonParentFolder() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-folder/1\"" +
                "        }}"));
        json.put("parent", new JSONObject("{" +
                "    \"_type\" : \"requirement-folder\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/requirement-folder/10\"" +
                "      }" +
                "    }}"));

        RequirementFolder requirementFolder = RequirementFolder.fromJson(json);
        Assert.assertEquals(requirementFolder.getId(), 1);
        Assert.assertEquals(requirementFolder.getName(), "foo");
        Assert.assertEquals(requirementFolder.getUrl(), "https://localhost:4321/requirement-folder/1");
        Assert.assertTrue(requirementFolder.getParent() instanceof RequirementFolder);
        Assert.assertEquals(requirementFolder.getParent().getId(), 10);
    }

    @Test
    public void testFromJsonParentIsProject() {

        JSONObject json = new JSONObject();
        json.put("_type", "requirement-folder");
        json.put("id", 1);
        json.put("name", "foo");
        json.put("_links", new JSONObject("{\"self\" : {" +
                "          \"href\" : \"https://localhost:4321/requirement-folder/1\"" +
                "        }}"));
        json.put("parent", new JSONObject("{" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Mangrove\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/project/10\"" +
                "      }" +
                "    }}"));

        RequirementFolder requirementFolder = RequirementFolder.fromJson(json);
        Assert.assertEquals(requirementFolder.getId(), 1);
        Assert.assertEquals(requirementFolder.getName(), "foo");
        Assert.assertEquals(requirementFolder.getUrl(), "https://localhost:4321/requirement-folder/1");
        Assert.assertTrue(requirementFolder.getParent() instanceof Project);
        Assert.assertEquals(requirementFolder.getParent().getId(), 10);
    }

    @Test
    public void testGetContent() {

        createServerMock("GET", "/requirement-folders/1010/content", 200, REQ_FOLDER_CONTENT);

        List<Entity> entities = RequirementFolder.getContent(1010);
        Assert.assertEquals(entities.size(), 7);
        Assert.assertEquals(entities.get(0).getType(), Entity.TYPE_REQUIREMENT);
        Assert.assertEquals(entities.get(6).getType(), Entity.TYPE_REQUIREMENT_FOLDER);
    }

    @Test
    public void testCreateRequirementFolderNoParent() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirement-folders", 200, SIMPLE_GET_REQ_FOLDER_TOP_LEVEL, "request");
        RequirementFolder requirementFolder = RequirementFolder.create(project, null, "embedded folder");
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"embedded folder\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        Assert.assertEquals(requirementFolder.getId(), 356);
        Assert.assertEquals(requirementFolder.getName(), "embedded folder");
        Assert.assertEquals(requirementFolder.getParent().getId(), 14);
    }

    @Test
    public void testCreateRequirementFolderWithParent() {
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirement-folders", 200, "{" +
                "  \"_type\" : \"requirement-folder\"," +
                "  \"id\" : 33," +
                "  \"name\" : \"Requirement folder 1\"," +
                "  \"project\" : {" +
                "    \"_type\" : \"project\"," +
                "    \"id\" : 14," +
                "    \"name\" : \"Test Project 1\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/projects/14\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"path\" : \"/Test Project 1/Requirement folder 1\"," +
                "  \"parent\" : {" +
                "    \"_type\" : \"requirement-folder\"," +
                "    \"id\" : 10," +
                "    \"name\" : \"Requirement folder parent\"," +
                "    \"_links\" : {" +
                "      \"self\" : {" +
                "        \"href\" : \"https://localhost:4321/requirement-folders/10\"" +
                "      }" +
                "    }" +
                "  }," +
                "  \"created_by\" : \"admin\"," +
                "  \"created_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"last_modified_by\" : \"admin\"," +
                "  \"last_modified_on\" : \"2017-06-15T10:00:00.000+0000\"," +
                "  \"description\" : null," +
                "  \"custom_fields\" : [ {" +
                "    \"code\" : \"cuf1\"," +
                "    \"label\" : \"Lib Cuf1\"," +
                "    \"value\" : \"Cuf1 Value\"" +
                "  }, {" +
                "    \"code\" : \"cuf2\"," +
                "    \"label\" : \"Lib Cuf2\"," +
                "    \"value\" : \"true\"" +
                "  } ]," +
                "  \"attachments\" : [ ]," +
                "  \"_links\" : {" +
                "    \"self\" : {" +
                "      \"href\" : \"https://localhost:4321/requirement-folders/33\"" +
                "    }," +
                "    \"project\" : {" +
                "      \"href\" : \"https://localhost:4321/projects/14\"" +
                "    }," +
                "    \"content\" : {" +
                "      \"href\" : \"https://localhost:4321/requirement-folders/33/content\"" +
                "    }," +
                "    \"attachments\" : {" +
                "      \"href\" : \"https://localhost:4321/requirement-folders/33/attachments\"" +
                "    }" +
                "  }" +
                "}", "request");
        RequirementFolder requirementFolderParent = new RequirementFolder("https://localhost:4321/requirement-folders/10", "requirement-folder", 10, "Requirement folder parent", project, null);
        RequirementFolder requirementFolder = RequirementFolder.create(project, requirementFolderParent, "Requirement folder 1");
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"Requirement folder 1\",\"parent\":{\"_type\":\"requirement-folder\",\"id\":10,\"name\":\"Requirement folder parent\"}}"));
        Assert.assertEquals(requirementFolder.getId(), 33);
        Assert.assertEquals(requirementFolder.getName(), "Requirement folder 1");
        Assert.assertEquals(requirementFolder.getParent().getId(), 10);
    }


    @Test(expectedExceptions = SquashTmException.class)
    public void testCreateRequirementFolderWithError() {
        RequestBodyEntity postRequest = (RequestBodyEntity) createServerMock("POST", "/requirement-folders", 200, "{}", "requestBodyEntity");
        when(postRequest.asJson()).thenThrow(UnirestException.class);

        RequirementFolder.create(project, null, "Requirement folder 1");
    }

    /**
     * Check we can create a new requirement folder
     */
    @Test
    public void testCreateRequirementFolderTree() {
        // requirement folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirement-folders", 200, REQ_FOLDER_CREATION_RESPONSE, "request");

        // requirement folder tree
        createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        RequirementFolder createdFolder = RequirementFolder.createRequirementFolderTree(project, "foo/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"foo\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"requirement-folder\",\"id\":33,\"name\":\"Requirement folder 1\"}}"));
    }

    /**
     * With cache, request to tree is done only once
     */
    @Test
    public void testCreateRequirementFolderTreeWithCache() {
        EntityCache.setEnabled(true);

        // requirement folder creation
        createServerMock("POST", "/requirement-folders", 200, REQ_FOLDER_CREATION_RESPONSE, "request");

        // requirement folder tree
        HttpRequest<?> getRequest = createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        RequirementFolder.createRequirementFolderTree(project, "foo/bar");
        RequirementFolder.createRequirementFolderTree(project, "foo/bar");

        verify(getRequest).asJson();

        // check cache contains the new requirement (only one, because mock always returns the same id)
        Assert.assertNotNull(RequirementFolder.getRequirementFolderCaches().get(project).get(33));
    }

    /**
     * With cache, request to tree is done twice
     */
    @Test
    public void testCreateRequirementFolderTreeWithoutCache() {
        EntityCache.setEnabled(false);

        // requirement folder creation
        createServerMock("POST", "/requirement-folders", 200, REQ_FOLDER_CREATION_RESPONSE, "request");

        // requirement folder tree
        HttpRequest<?> getRequest = createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        RequirementFolder.createRequirementFolderTree(project, "foo/bar");
        RequirementFolder.createRequirementFolderTree(project, "foo/bar");

        verify(getRequest, atLeast(2)).asJson();

        // check cache does not contain the new requirement
        Assert.assertNull(RequirementFolder.getRequirementFolderCaches().get(project).get(33));
    }

    @Test
    public void testCreateRequirementFolderTreeNoFolder() {
        // requirement folder creation
        createServerMock("POST", "/requirement-folders", 200, SIMPLE_GET_REQ_FOLDER, "request");

        // requirement folder tree
        createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        RequirementFolder createdFolder = RequirementFolder.createRequirementFolderTree(project, (String) null);
        Assert.assertNull(createdFolder);
    }

    /**
     * When a folder already exist do not recreate it
     */
    @Test
    public void testCreateRequirementFolderTreeAlreadyExist() {
        // requirement folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirement-folders", 200, REQ_FOLDER_CREATION_RESPONSE, "request");

        // campaign folder tree
        createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_TREE_PROJECT);

        createServerMock("GET", "/requirement-folders/33", 200, REQ_FOLDER_100_REPLY_DATA);

        RequirementFolder createdFolder = RequirementFolder.createRequirementFolderTree(project, "folder1/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest, never()).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"folder1\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"requirement-folder\",\"id\":100,\"name\":\"folder1\"}}"));
    }

    @Test
    public void testCreateRequirementFolderTreeAlreadyExist2() {
        // requirement folder creation
        HttpRequestWithBody postRequest = (HttpRequestWithBody) createServerMock("POST", "/requirement-folders", 200, REQ_FOLDER_CREATION_RESPONSE, "request");

        // requirement folder tree
        createServerMock("GET", "/requirement-folders/tree/14", 200, REQ_FOLDER_SUBFOLDER_RESPONSE);

        createServerMock("GET", "/requirement-folders/33", 200, REQ_FOLDER_100_REPLY_DATA);

        RequirementFolder createdFolder = RequirementFolder.createRequirementFolderTree(project, "folder1/sub-folder1/bar");
        Assert.assertEquals(createdFolder.getId(), 33);

        // check 2 folders has been created
        verify(postRequest, never()).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"folder1\",\"parent\":{\"_type\":\"project\",\"id\":14,\"name\":\"Test Project 1\"}}"));
        verify(postRequest).body(new JSONObject("{\"_type\":\"requirement-folder\",\"name\":\"bar\",\"parent\":{\"_type\":\"requirement-folder\",\"id\":1000,\"name\":\"sub-folder1\"}}"));
    }

    @Test
    public void testGetFromId() {
        createServerMock("GET", "/requirement-folders/100", 200, REQ_FOLDER_100_REPLY_DATA);
        RequirementFolder requirementFolder = RequirementFolder.get(100);
        Assert.assertEquals(requirementFolder.getName(), "folder1");
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "Requirement folder 100 does not exist")
    public void testGetFromIdWithError() {
        GetRequest getRequest = (GetRequest) createServerMock("GET", "/requirement-folders/100", 200, "{}");
        when(getRequest.asJson()).thenThrow(new UnirestException("Cannot get"));
        RequirementFolder requirementFolder = RequirementFolder.get(100);
    }

    @Test(expectedExceptions = SquashTmException.class, expectedExceptionsMessageRegExp = "request to https://localhost:4321 failed\\[500\\]: null")
    public void testGetFromIdWithError2() {
        createServerMock("GET", "/requirement-folders/100", 500, "{}");
        RequirementFolder requirementFolder = RequirementFolder.get(100);
    }

    @Test(expectedExceptions = NotImplementedException.class)
    public void testCompleteDetails() {
        createServerMock("GET", "/requirement-folders/100", 200, REQ_FOLDER_100_REPLY_DATA);
        RequirementFolder requirementFolder = RequirementFolder.get(100);
        requirementFolder.completeDetails();
    }

    @Test
    public void testSetters() {
        createServerMock("GET", "/requirement-folders/100", 200, REQ_FOLDER_100_REPLY_DATA);
        createServerMock("GET", "/requirement-folders/101", 200, REQ_FOLDER_101_REPLY_DATA);
        RequirementFolder requirementFolder = RequirementFolder.get(100);
        requirementFolder.setParent(RequirementFolder.get(101));
        requirementFolder.setProject(new Project("url", "project", 1281, "project test"));
        Assert.assertEquals(requirementFolder.getParent().getId(), 101);
        Assert.assertEquals(requirementFolder.getParent().getName(), "folder2");
        Assert.assertEquals(requirementFolder.getProject().getId(), 1281);
        Assert.assertEquals(requirementFolder.getProject().getName(), "project test");
    }

}
