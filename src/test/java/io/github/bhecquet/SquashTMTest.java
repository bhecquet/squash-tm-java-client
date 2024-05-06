/**
 * Orignal work: Copyright 2015 www.seleniumtests.com
 * Modified work: Copyright 2016 www.infotel.com
 * Copyright 2017-2019 B.Hecquet
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.bhecquet;

import io.github.bhecquet.exceptions.TestConfigurationException;
import kong.unirest.core.*;
import kong.unirest.core.json.JSONArray;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class SquashTMTest {

    protected static final String SERVER_URL = "https://localhost:4321";

    // mocks
    public GetRequest getAliveRequest;
    public Config unirestConfig;
    public HttpRequestWithBody postRequest;
    public HttpResponse<String> responseAliveString;
    public UnirestInstance unirestInstance;

    protected static ThreadLocal<MockedStatic> mockedUnirest = new ThreadLocal<>();
    protected MockedStatic mockedWebUiDriverFactory;
    protected MockedConstruction mockedWebUiDriver;
    protected MockedConstruction mockedRemoteWebDriver;

    protected GetRequest namedApplicationRequest;
    protected GetRequest namedEnvironmentRequest;
    protected GetRequest namedTestCaseRequest;
    protected GetRequest namedVersionRequest;
    protected GetRequest variablesRequest;
    protected HttpRequestWithBody createApplicationRequest;
    protected HttpRequestWithBody createEnvironmentRequest;
    protected HttpRequestWithBody createVersionRequest;
    protected HttpRequestWithBody createTestCaseRequest;
    protected HttpRequestWithBody createVariableRequest;
    protected HttpRequestWithBody updateVariableRequest;
    protected HttpRequestWithBody updateVariableRequest2;
    protected HttpRequestWithBody deleteVariableRequest;

    @BeforeMethod
    public void initMocks(final Method method, final ITestContext testNGCtx, final ITestResult testResult) throws Exception {

        MockitoAnnotations.initMocks(this);

        getAliveRequest = mock(GetRequest.class);
        unirestConfig = mock(Config.class);
        postRequest = mock(HttpRequestWithBody.class);
        responseAliveString = mock(HttpResponse.class);
        unirestInstance = mock(UnirestInstance.class);

        // for multi-thread tests (typically TestErrorCauseFinder), we need to initialize a ConnectorsTest instance at subTest start
        // so discard the mock for current thread and recreate it for our instance
        try {
            mockedUnirest.set(mockStatic(Unirest.class));
        } catch (MockitoException e) {
            if (mockedUnirest.get() != null) {
                mockedUnirest.get().close();
                mockedUnirest.set(mockStatic(Unirest.class));
            }
        }
        mockedUnirest.get().when(() -> Unirest.spawnInstance()).thenReturn(unirestInstance);
        mockedUnirest.get().when(() -> Unirest.config()).thenReturn(unirestConfig);


    }

    @AfterMethod(alwaysRun = true)
    public void resetMocks() {
        if (mockedUnirest.get() != null) {
            mockedUnirest.get().close(); // as we do not use try-with-resource
            mockedUnirest.remove();
        }

        if (mockedWebUiDriver != null) {
            mockedWebUiDriver.close();
            mockedWebUiDriver = null;
        }
        if (mockedRemoteWebDriver != null) {
            mockedRemoteWebDriver.close();
            mockedRemoteWebDriver = null;
        }
        if (mockedWebUiDriverFactory != null) {
            mockedWebUiDriverFactory.close();
            mockedWebUiDriverFactory = null;
        }
    }

    /**
     * Method for creating server reply mock
     *
     * @throws UnirestException
     */

    public HttpRequest<?> createServerMock(String requestType, String apiPath, int statusCode, String replyData) throws UnirestException {
        return createServerMock(requestType, apiPath, statusCode, replyData, "request");
    }

    public HttpRequest<?> createServerMock(String requestType, String apiPath, int statusCode, File replyData) throws UnirestException {
        return createServerMock(requestType, apiPath, statusCode, replyData, "request");
    }

    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, String replyData) throws UnirestException {
        return createServerMock(serverUrl, requestType, apiPath, statusCode, replyData, "request");
    }

    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, File replyData) throws UnirestException {
        return createServerMock(serverUrl, requestType, apiPath, statusCode, replyData, "request");
    }

    /**
     * @param requestType
     * @param apiPath
     * @param statusCode
     * @param replyData
     * @param responseType if "request", replies with the POST request object (HttpRequestWithBody.class). If "body", replies with the body (MultipartBody.class)
     * @return
     * @throws UnirestException
     */
    public HttpRequest<?> createServerMock(String requestType, String apiPath, int statusCode, File replyData, String responseType) throws UnirestException {
        return createServerMock(SERVER_URL, requestType, apiPath, statusCode, (Object) replyData, responseType);
    }

    public HttpRequest<?> createServerMock(String requestType, String apiPath, int statusCode, String replyData, String responseType) throws UnirestException {
        return createServerMock(SERVER_URL, requestType, apiPath, statusCode, (Object) replyData, responseType);
    }

    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, File replyData, String responseType) throws UnirestException {
        return createServerMock(serverUrl, requestType, apiPath, statusCode, (Object) replyData, responseType);
    }

    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, String replyData, String responseType) throws UnirestException {
        return createServerMock(serverUrl, requestType, apiPath, statusCode, (Object) replyData, responseType);
    }

    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, Object replyData, String responseType) throws UnirestException {
        return createServerMock(serverUrl, requestType, apiPath, statusCode, Arrays.asList(replyData), responseType);
    }

    /**
     * @param serverUrl    URL of the mock server
     * @param requestType  GET, POST, HEAD, ...
     * @param apiPath      the endpoint on the mock server (ex: /api/foo/bar)
     * @param statusCode   the status code to return: 200, 500, ...
     * @param replyData    the list of response data. In case service is called more times than the number of provided responses, the last one will be repeated
     * @param responseType "request", "requestBodyEntity", "body". if "request", replies with the POST request object (HttpRequestWithBody.class). If "body", replies with the body (MultipartBody.class)
     * @return
     * @throws UnirestException
     */
    public HttpRequest<?> createServerMock(String serverUrl, String requestType, String apiPath, int statusCode, final List<Object> replyData, String responseType) throws UnirestException {


        if (replyData.isEmpty()) {
            throw new TestConfigurationException("No replyData specified");
        }

        @SuppressWarnings("unchecked")
        HttpResponse<String> response = mock(HttpResponse.class);
        HttpResponse<JsonNode> jsonResponse = mock(HttpResponse.class);
        HttpResponse<File> streamResponse = mock(HttpResponse.class);
        HttpResponse<byte[]> bytestreamResponse = mock(HttpResponse.class);
        HttpRequest<?> request = mock(HttpRequest.class);
        JsonNode json = mock(JsonNode.class);
        HttpRequestWithBody postRequest = spy(HttpRequestWithBody.class);

        PagedList<JsonNode> pageList = new PagedList<>(); // for asPaged method

        when(request.getUrl()).thenReturn(serverUrl);
        if (replyData.get(0) instanceof String) {
            when(response.getStatus()).thenReturn(statusCode);
            //when(response.getBody()).thenReturn(replyData.toArray(new String[] {}));

            when(response.getBody()).then(new Answer<String>() {
                private int count = -1;

                public String answer(InvocationOnMock invocation) {

                    count++;
                    if (count >= replyData.size() - 1) {
                        return (String) replyData.get(replyData.size() - 1);
                    } else {
                        return (String) replyData.get(count);
                    }
                }
            });
            when(response.getStatusText()).thenReturn("TEXT");

            when(jsonResponse.getStatus()).thenReturn(statusCode);
            when(jsonResponse.getBody()).thenReturn(json);
            when(jsonResponse.getStatusText()).thenReturn("TEXT");
            try {
                // check data is compatible with JSON
                for (Object d : replyData) {
                    if (((String) d).isEmpty()) {
                        d = "{}";
                    }
                    try {
                        new JSONObject((String) d);
                    } catch (JSONException e) {
                        new JSONArray((String) d);
                    }
                }


                //				JSONObject jsonReply = new JSONObject((String)replyData);
                //				when(json.getObject()).thenReturn(jsonReply);

                when(json.getObject()).then(new Answer<JSONObject>() {
                    private int count = -1;

                    public JSONObject answer(InvocationOnMock invocation) {

                        count++;
                        String reply;
                        if (count >= replyData.size() - 1) {
                            reply = (String) replyData.get(replyData.size() - 1);
                        } else {
                            reply = (String) replyData.get(count);
                        }
                        if (reply.isEmpty()) {
                            reply = "{}";
                        }
                        return new JSONObject(reply);
                    }
                });

                when(json.getArray()).then(new Answer<JSONArray>() {
                    private int count = -1;

                    public JSONArray answer(InvocationOnMock invocation) {

                        count++;
                        String reply;
                        if (count >= replyData.size() - 1) {
                            reply = (String) replyData.get(replyData.size() - 1);
                        } else {
                            reply = (String) replyData.get(count);
                        }
                        if (reply.isEmpty()) {
                            reply = "{}";
                        }
                        return new JSONArray(reply);
                    }
                });

                pageList = new PagedList<>();
                pageList.add(jsonResponse);

            } catch (JSONException | NullPointerException e) {
            }


        } else if (replyData.get(0) instanceof File) {
            when(streamResponse.getStatus()).thenReturn(statusCode);
            when(streamResponse.getStatusText()).thenReturn("TEXT");
            when(streamResponse.getBody()).then(new Answer<File>() {
                private int count = -1;

                public File answer(InvocationOnMock invocation) {

                    count++;
                    if (count >= replyData.size() - 1) {
                        return (File) replyData.get(replyData.size() - 1);
                    } else {
                        return (File) replyData.get(count);
                    }
                }
            });

            /*when(bytestreamResponse.getBody()).then(new Answer<byte[]>() {
                private int count = -1;

                public byte[] answer(InvocationOnMock invocation) throws IOException {

                    count++;
                    if (count >= replyData.size() - 1) {
                        return (byte[]) FileUtils.readFileToByteArray((File) replyData.get(replyData.size() - 1));
                    } else {
                        return (byte[]) FileUtils.readFileToByteArray((File) replyData.get(count));
                    }
                }
            });*/

            when(bytestreamResponse.getStatus()).thenReturn(statusCode);
            when(bytestreamResponse.getStatusText()).thenReturn("BYTES");

        }


        switch (requestType) {
            case "GET":
                GetRequest getRequest = mock(GetRequest.class);

                mockedUnirest.get().when(() -> Unirest.get(serverUrl + apiPath)).thenReturn(getRequest);
                when(getRequest.downloadMonitor(any())).thenReturn(getRequest);
                when(unirestInstance.get(serverUrl + apiPath)).thenReturn(getRequest);

                when(getRequest.header(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.asString()).thenReturn(response);
                when(getRequest.asJson()).thenReturn(jsonResponse);
                when(getRequest.asFile(anyString())).thenReturn(streamResponse);
                when(getRequest.asFile(anyString(), any(StandardCopyOption.class))).thenReturn(streamResponse);
                when(getRequest.asBytes()).thenReturn(bytestreamResponse);
                when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.queryString(anyString(), anyInt())).thenReturn(getRequest);
                when(getRequest.queryString(anyString(), anyBoolean())).thenReturn(getRequest);
                when(getRequest.getUrl()).thenReturn(serverUrl);
                when(getRequest.basicAuth(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.headerReplace(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.asPaged(any(), (Function<HttpResponse<JsonNode>, String>) any(Function.class))).thenReturn(pageList);
                return getRequest;
            case "POST":
                mockedUnirest.get().when(() -> Unirest.post(serverUrl + apiPath)).thenReturn(postRequest);
                when(unirestInstance.post(serverUrl + apiPath)).thenReturn(postRequest);
                return preparePostRequest(serverUrl, responseType, postRequest, response, jsonResponse);
            case "PATCH":
                mockedUnirest.get().when(() -> Unirest.patch(serverUrl + apiPath)).thenReturn(postRequest);
                when(unirestInstance.patch(serverUrl + apiPath)).thenReturn(postRequest);
                return preparePostRequest(serverUrl, responseType, postRequest, response, jsonResponse);
            case "PUT":
                mockedUnirest.get().when(() -> Unirest.put(serverUrl + apiPath)).thenReturn(postRequest);
                when(unirestInstance.put(serverUrl + apiPath)).thenReturn(postRequest);
                return preparePostRequest(serverUrl, responseType, postRequest, response, jsonResponse);
            case "DELETE":
                mockedUnirest.get().when(() -> Unirest.delete(serverUrl + apiPath)).thenReturn(postRequest);
                when(unirestInstance.delete(serverUrl + apiPath)).thenReturn(postRequest);
                return preparePostRequest(serverUrl, responseType, postRequest, response, jsonResponse);

        }
        return null;

    }

    private HttpRequest<?> preparePostRequest(String serverUrl, String responseType, HttpRequestWithBody postRequest, HttpResponse<String> response, HttpResponse<JsonNode> jsonResponse) {

        RequestBodyEntity requestBodyEntity = mock(RequestBodyEntity.class);
        MultipartBody requestMultipartBody = mock(MultipartBody.class);

        when(postRequest.field(anyString(), anyString())).thenReturn(requestMultipartBody);
        when(postRequest.field(anyString(), anyInt())).thenReturn(requestMultipartBody);
        when(postRequest.field(anyString(), anyLong())).thenReturn(requestMultipartBody);
        when(postRequest.field(anyString(), anyDouble())).thenReturn(requestMultipartBody);
        when(postRequest.field(anyString(), any(File.class))).thenReturn(requestMultipartBody);
        when(postRequest.basicAuth(anyString(), anyString())).thenReturn(postRequest);
        when(postRequest.headerReplace(anyString(), anyString())).thenReturn(postRequest);
        when(postRequest.queryString(anyString(), anyString())).thenReturn(postRequest);
        when(postRequest.queryString(anyString(), anyInt())).thenReturn(postRequest);
        when(postRequest.queryString(anyString(), anyBoolean())).thenReturn(postRequest);
        when(postRequest.header(anyString(), anyString())).thenReturn(postRequest);
        when(requestMultipartBody.field(anyString(), anyString())).thenReturn(requestMultipartBody);
        when(requestMultipartBody.field(anyString(), any(File.class))).thenReturn(requestMultipartBody);
        when(requestMultipartBody.asString()).thenReturn(response);
        doReturn(response).when(postRequest).asString();
        when(postRequest.getUrl()).thenReturn(serverUrl);
        when(postRequest.body(any(JSONObject.class))).thenReturn(requestBodyEntity);
        when(postRequest.body(any(byte[].class))).thenReturn(requestBodyEntity);
        when(postRequest.asJson()).thenReturn(jsonResponse);
        when(requestBodyEntity.asJson()).thenReturn(jsonResponse);
        when(requestBodyEntity.asString()).thenReturn(response);
        when(requestMultipartBody.getUrl()).thenReturn(serverUrl);
        when(requestMultipartBody.asJson()).thenReturn(jsonResponse);

        if ("request".equals(responseType)) {
            return postRequest;
        } else if ("body".equals(responseType)) {
            return requestMultipartBody;
        } else if ("requestBodyEntity".equals(responseType)) {
            return requestBodyEntity;
        } else {
            return null;
        }
    }

    protected OngoingStubbing<JsonNode> createJsonServerMock(String requestType, String apiPath, int statusCode, String... replyData) throws UnirestException {

        @SuppressWarnings("unchecked")
        HttpResponse<JsonNode> jsonResponse = mock(HttpResponse.class);
        HttpRequest<?> request = mock(HttpRequest.class);
        MultipartBody requestMultipartBody = mock(MultipartBody.class);
        HttpRequestWithBody postRequest = mock(HttpRequestWithBody.class);

        when(request.getUrl()).thenReturn(SERVER_URL);
        when(jsonResponse.getStatus()).thenReturn(statusCode);

        OngoingStubbing<JsonNode> stub = when(jsonResponse.getBody()).thenReturn(new JsonNode(replyData[0]));

        for (String reply : Arrays.asList(replyData).subList(1, replyData.length)) {
            stub = stub.thenReturn(new JsonNode(reply));
        }


        switch (requestType) {
            case "GET":
                GetRequest getRequest = mock(GetRequest.class);

                mockedUnirest.get().when(() -> Unirest.get(SERVER_URL + apiPath)).thenReturn(getRequest);

                when(getRequest.header(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.asJson()).thenReturn(jsonResponse);
                when(getRequest.queryString(anyString(), anyString())).thenReturn(getRequest);
                when(getRequest.queryString(anyString(), anyInt())).thenReturn(getRequest);
                when(getRequest.queryString(anyString(), anyBoolean())).thenReturn(getRequest);
                return stub;
            case "POST":
                mockedUnirest.get().when(() -> Unirest.post(SERVER_URL + apiPath)).thenReturn(postRequest);
            case "PATCH":
                mockedUnirest.get().when(() -> Unirest.patch(SERVER_URL + apiPath)).thenReturn(postRequest);
                when(postRequest.field(anyString(), anyString())).thenReturn(requestMultipartBody);
                when(postRequest.field(anyString(), anyInt())).thenReturn(requestMultipartBody);
                when(postRequest.field(anyString(), anyLong())).thenReturn(requestMultipartBody);
                when(postRequest.field(anyString(), any(File.class))).thenReturn(requestMultipartBody);
                when(postRequest.queryString(anyString(), anyString())).thenReturn(postRequest);
                when(postRequest.queryString(anyString(), anyInt())).thenReturn(postRequest);
                when(postRequest.queryString(anyString(), anyBoolean())).thenReturn(postRequest);
                when(postRequest.header(anyString(), anyString())).thenReturn(postRequest);
                when(requestMultipartBody.field(anyString(), anyString())).thenReturn(requestMultipartBody);
                when(requestMultipartBody.field(anyString(), any(File.class))).thenReturn(requestMultipartBody);
                return stub;

        }

        return null;
    }

}
