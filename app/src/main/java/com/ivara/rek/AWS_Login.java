package com.ivara.rek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.http.HttpMethodName;

import com.amazonaws.mobile.api.idnz6pgle06i.idnz6pgle06i.ExplorerMobileHubClient;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AWS_Login extends AppCompatActivity {

    private ExplorerMobileHubClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aws__login);

        apiClient = new ApiClientFactory()
                .credentialsProvider(AWSMobileClient.getInstance().getCredentialsProvider())
                .build(ExplorerMobileHubClient.class);

        callCloudLogic();

    }

    public void callCloudLogic() {
        final String method = "GET";
        final String path= "/items";
        final String body = "";
        final byte[] content = body.getBytes(StringUtils.UTF8);

        final Map parameters = new HashMap<>();
        parameters.put("lang", "en_US");

        final Map Headers = new HashMap<>();

        ApiRequest localRequest = new ApiRequest(apiClient.getClass().getSimpleName())
                .withPath(path)
                .withHttpMethod(HttpMethodName.valueOf(method))
                .withHeaders(Headers)
                .addHeader("Content-Type", "application/json")
                .withParameters(parameters);

        if (body.length() > 0 ){
            localRequest = localRequest
                    .addHeader("Content-Length", String.valueOf(content.length))
                    .withBody(content);
        }

        final ApiRequest request = localRequest;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("AWS CLOUD LOG",
                            "Invoking API w/ Request : " +
                                    request.getHttpMethod() + ":" +
                                    request.getPath());

                    final ApiResponse response = apiClient.execute(request);

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null ){
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d("AWS CLOUD LOG", "Response : " + responseData);

                    }

                    Log.d("AWS CLOUD LOG", response.getStatusCode() + " " + response.getStatusText());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
