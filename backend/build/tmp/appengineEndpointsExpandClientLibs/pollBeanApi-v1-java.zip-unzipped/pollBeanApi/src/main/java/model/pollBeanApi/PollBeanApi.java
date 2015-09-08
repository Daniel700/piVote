/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-08-03 17:34:38 UTC)
 * on 2015-09-08 at 12:11:52 UTC 
 * Modify at your own risk.
 */

package model.pollBeanApi;

/**
 * Service definition for PollBeanApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link PollBeanApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class PollBeanApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.20.0 of the pollBeanApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://myApplicationId.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "pollBeanApi/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public PollBeanApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  PollBeanApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getBatchPollBeans".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link GetBatchPollBeans#execute()} method to invoke the remote operation.
   *
   * @param ids
   * @return the request
   */
  public GetBatchPollBeans getBatchPollBeans(java.util.List<java.lang.Long> ids) throws java.io.IOException {
    GetBatchPollBeans result = new GetBatchPollBeans(ids);
    initialize(result);
    return result;
  }

  public class GetBatchPollBeans extends PollBeanApiRequest<model.pollBeanApi.model.PollBeanCollection> {

    private static final String REST_PATH = "getBatchPolls";

    /**
     * Create a request for the method "getBatchPollBeans".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link GetBatchPollBeans#execute()} method to invoke the remote
     * operation. <p> {@link GetBatchPollBeans#initialize(com.google.api.client.googleapis.services.Ab
     * stractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param ids
     * @since 1.13
     */
    protected GetBatchPollBeans(java.util.List<java.lang.Long> ids) {
      super(PollBeanApi.this, "POST", REST_PATH, null, model.pollBeanApi.model.PollBeanCollection.class);
      this.ids = com.google.api.client.util.Preconditions.checkNotNull(ids, "Required parameter ids must be specified.");
    }

    @Override
    public GetBatchPollBeans setAlt(java.lang.String alt) {
      return (GetBatchPollBeans) super.setAlt(alt);
    }

    @Override
    public GetBatchPollBeans setFields(java.lang.String fields) {
      return (GetBatchPollBeans) super.setFields(fields);
    }

    @Override
    public GetBatchPollBeans setKey(java.lang.String key) {
      return (GetBatchPollBeans) super.setKey(key);
    }

    @Override
    public GetBatchPollBeans setOauthToken(java.lang.String oauthToken) {
      return (GetBatchPollBeans) super.setOauthToken(oauthToken);
    }

    @Override
    public GetBatchPollBeans setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetBatchPollBeans) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetBatchPollBeans setQuotaUser(java.lang.String quotaUser) {
      return (GetBatchPollBeans) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetBatchPollBeans setUserIp(java.lang.String userIp) {
      return (GetBatchPollBeans) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.util.List<java.lang.Long> ids;

    /**

     */
    public java.util.List<java.lang.Long> getIds() {
      return ids;
    }

    public GetBatchPollBeans setIds(java.util.List<java.lang.Long> ids) {
      this.ids = ids;
      return this;
    }

    @Override
    public GetBatchPollBeans set(String parameterName, Object value) {
      return (GetBatchPollBeans) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getMyPollBeans".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link GetMyPollBeans#execute()} method to invoke the remote operation.
   *
   * @param uuid
   * @return the request
   */
  public GetMyPollBeans getMyPollBeans(java.lang.String uuid) throws java.io.IOException {
    GetMyPollBeans result = new GetMyPollBeans(uuid);
    initialize(result);
    return result;
  }

  public class GetMyPollBeans extends PollBeanApiRequest<model.pollBeanApi.model.PollBeanCollection> {

    private static final String REST_PATH = "myPolls";

    /**
     * Create a request for the method "getMyPollBeans".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link GetMyPollBeans#execute()} method to invoke the remote
     * operation. <p> {@link GetMyPollBeans#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param uuid
     * @since 1.13
     */
    protected GetMyPollBeans(java.lang.String uuid) {
      super(PollBeanApi.this, "GET", REST_PATH, null, model.pollBeanApi.model.PollBeanCollection.class);
      this.uuid = com.google.api.client.util.Preconditions.checkNotNull(uuid, "Required parameter uuid must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetMyPollBeans setAlt(java.lang.String alt) {
      return (GetMyPollBeans) super.setAlt(alt);
    }

    @Override
    public GetMyPollBeans setFields(java.lang.String fields) {
      return (GetMyPollBeans) super.setFields(fields);
    }

    @Override
    public GetMyPollBeans setKey(java.lang.String key) {
      return (GetMyPollBeans) super.setKey(key);
    }

    @Override
    public GetMyPollBeans setOauthToken(java.lang.String oauthToken) {
      return (GetMyPollBeans) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMyPollBeans setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMyPollBeans) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMyPollBeans setQuotaUser(java.lang.String quotaUser) {
      return (GetMyPollBeans) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMyPollBeans setUserIp(java.lang.String userIp) {
      return (GetMyPollBeans) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String uuid;

    /**

     */
    public java.lang.String getUuid() {
      return uuid;
    }

    public GetMyPollBeans setUuid(java.lang.String uuid) {
      this.uuid = uuid;
      return this;
    }

    @Override
    public GetMyPollBeans set(String parameterName, Object value) {
      return (GetMyPollBeans) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getPollBean".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link GetPollBean#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetPollBean getPollBean(java.lang.Long id) throws java.io.IOException {
    GetPollBean result = new GetPollBean(id);
    initialize(result);
    return result;
  }

  public class GetPollBean extends PollBeanApiRequest<model.pollBeanApi.model.PollBean> {

    private static final String REST_PATH = "getPoll";

    /**
     * Create a request for the method "getPollBean".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link GetPollBean#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetPollBean#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetPollBean(java.lang.Long id) {
      super(PollBeanApi.this, "GET", REST_PATH, null, model.pollBeanApi.model.PollBean.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetPollBean setAlt(java.lang.String alt) {
      return (GetPollBean) super.setAlt(alt);
    }

    @Override
    public GetPollBean setFields(java.lang.String fields) {
      return (GetPollBean) super.setFields(fields);
    }

    @Override
    public GetPollBean setKey(java.lang.String key) {
      return (GetPollBean) super.setKey(key);
    }

    @Override
    public GetPollBean setOauthToken(java.lang.String oauthToken) {
      return (GetPollBean) super.setOauthToken(oauthToken);
    }

    @Override
    public GetPollBean setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetPollBean) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetPollBean setQuotaUser(java.lang.String quotaUser) {
      return (GetPollBean) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetPollBean setUserIp(java.lang.String userIp) {
      return (GetPollBean) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetPollBean setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetPollBean set(String parameterName, Object value) {
      return (GetPollBean) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getRandomPollBeans".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link GetRandomPollBeans#execute()} method to invoke the remote operation.
   *
   * @param category
   * @param language
   * @return the request
   */
  public GetRandomPollBeans getRandomPollBeans(java.lang.String category, java.lang.String language) throws java.io.IOException {
    GetRandomPollBeans result = new GetRandomPollBeans(category, language);
    initialize(result);
    return result;
  }

  public class GetRandomPollBeans extends PollBeanApiRequest<model.pollBeanApi.model.PollBeanCollection> {

    private static final String REST_PATH = "randomPolls";

    /**
     * Create a request for the method "getRandomPollBeans".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link GetRandomPollBeans#execute()} method to invoke the remote
     * operation. <p> {@link GetRandomPollBeans#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param category
     * @param language
     * @since 1.13
     */
    protected GetRandomPollBeans(java.lang.String category, java.lang.String language) {
      super(PollBeanApi.this, "GET", REST_PATH, null, model.pollBeanApi.model.PollBeanCollection.class);
      this.category = com.google.api.client.util.Preconditions.checkNotNull(category, "Required parameter category must be specified.");
      this.language = com.google.api.client.util.Preconditions.checkNotNull(language, "Required parameter language must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetRandomPollBeans setAlt(java.lang.String alt) {
      return (GetRandomPollBeans) super.setAlt(alt);
    }

    @Override
    public GetRandomPollBeans setFields(java.lang.String fields) {
      return (GetRandomPollBeans) super.setFields(fields);
    }

    @Override
    public GetRandomPollBeans setKey(java.lang.String key) {
      return (GetRandomPollBeans) super.setKey(key);
    }

    @Override
    public GetRandomPollBeans setOauthToken(java.lang.String oauthToken) {
      return (GetRandomPollBeans) super.setOauthToken(oauthToken);
    }

    @Override
    public GetRandomPollBeans setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetRandomPollBeans) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetRandomPollBeans setQuotaUser(java.lang.String quotaUser) {
      return (GetRandomPollBeans) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetRandomPollBeans setUserIp(java.lang.String userIp) {
      return (GetRandomPollBeans) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String category;

    /**

     */
    public java.lang.String getCategory() {
      return category;
    }

    public GetRandomPollBeans setCategory(java.lang.String category) {
      this.category = category;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String language;

    /**

     */
    public java.lang.String getLanguage() {
      return language;
    }

    public GetRandomPollBeans setLanguage(java.lang.String language) {
      this.language = language;
      return this;
    }

    @Override
    public GetRandomPollBeans set(String parameterName, Object value) {
      return (GetRandomPollBeans) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getTop100PollBeans".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link GetTop100PollBeans#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetTop100PollBeans getTop100PollBeans() throws java.io.IOException {
    GetTop100PollBeans result = new GetTop100PollBeans();
    initialize(result);
    return result;
  }

  public class GetTop100PollBeans extends PollBeanApiRequest<model.pollBeanApi.model.PollBeanCollection> {

    private static final String REST_PATH = "top100Polls";

    /**
     * Create a request for the method "getTop100PollBeans".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link GetTop100PollBeans#execute()} method to invoke the remote
     * operation. <p> {@link GetTop100PollBeans#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetTop100PollBeans() {
      super(PollBeanApi.this, "GET", REST_PATH, null, model.pollBeanApi.model.PollBeanCollection.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetTop100PollBeans setAlt(java.lang.String alt) {
      return (GetTop100PollBeans) super.setAlt(alt);
    }

    @Override
    public GetTop100PollBeans setFields(java.lang.String fields) {
      return (GetTop100PollBeans) super.setFields(fields);
    }

    @Override
    public GetTop100PollBeans setKey(java.lang.String key) {
      return (GetTop100PollBeans) super.setKey(key);
    }

    @Override
    public GetTop100PollBeans setOauthToken(java.lang.String oauthToken) {
      return (GetTop100PollBeans) super.setOauthToken(oauthToken);
    }

    @Override
    public GetTop100PollBeans setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetTop100PollBeans) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetTop100PollBeans setQuotaUser(java.lang.String quotaUser) {
      return (GetTop100PollBeans) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetTop100PollBeans setUserIp(java.lang.String userIp) {
      return (GetTop100PollBeans) super.setUserIp(userIp);
    }

    @Override
    public GetTop100PollBeans set(String parameterName, Object value) {
      return (GetTop100PollBeans) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertPollBean".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link InsertPollBean#execute()} method to invoke the remote operation.
   *
   * @param content the {@link model.pollBeanApi.model.PollBean}
   * @return the request
   */
  public InsertPollBean insertPollBean(model.pollBeanApi.model.PollBean content) throws java.io.IOException {
    InsertPollBean result = new InsertPollBean(content);
    initialize(result);
    return result;
  }

  public class InsertPollBean extends PollBeanApiRequest<model.pollBeanApi.model.PollBean> {

    private static final String REST_PATH = "insertPoll";

    /**
     * Create a request for the method "insertPollBean".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link InsertPollBean#execute()} method to invoke the remote
     * operation. <p> {@link InsertPollBean#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link model.pollBeanApi.model.PollBean}
     * @since 1.13
     */
    protected InsertPollBean(model.pollBeanApi.model.PollBean content) {
      super(PollBeanApi.this, "POST", REST_PATH, content, model.pollBeanApi.model.PollBean.class);
    }

    @Override
    public InsertPollBean setAlt(java.lang.String alt) {
      return (InsertPollBean) super.setAlt(alt);
    }

    @Override
    public InsertPollBean setFields(java.lang.String fields) {
      return (InsertPollBean) super.setFields(fields);
    }

    @Override
    public InsertPollBean setKey(java.lang.String key) {
      return (InsertPollBean) super.setKey(key);
    }

    @Override
    public InsertPollBean setOauthToken(java.lang.String oauthToken) {
      return (InsertPollBean) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertPollBean setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertPollBean) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertPollBean setQuotaUser(java.lang.String quotaUser) {
      return (InsertPollBean) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertPollBean setUserIp(java.lang.String userIp) {
      return (InsertPollBean) super.setUserIp(userIp);
    }

    @Override
    public InsertPollBean set(String parameterName, Object value) {
      return (InsertPollBean) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updatePollBean".
   *
   * This request holds the parameters needed by the pollBeanApi server.  After setting any optional
   * parameters, call the {@link UpdatePollBean#execute()} method to invoke the remote operation.
   *
   * @param selectedAnswer
   * @param content the {@link model.pollBeanApi.model.PollBean}
   * @return the request
   */
  public UpdatePollBean updatePollBean(java.lang.String selectedAnswer, model.pollBeanApi.model.PollBean content) throws java.io.IOException {
    UpdatePollBean result = new UpdatePollBean(selectedAnswer, content);
    initialize(result);
    return result;
  }

  public class UpdatePollBean extends PollBeanApiRequest<Void> {

    private static final String REST_PATH = "updatePoll";

    /**
     * Create a request for the method "updatePollBean".
     *
     * This request holds the parameters needed by the the pollBeanApi server.  After setting any
     * optional parameters, call the {@link UpdatePollBean#execute()} method to invoke the remote
     * operation. <p> {@link UpdatePollBean#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param selectedAnswer
     * @param content the {@link model.pollBeanApi.model.PollBean}
     * @since 1.13
     */
    protected UpdatePollBean(java.lang.String selectedAnswer, model.pollBeanApi.model.PollBean content) {
      super(PollBeanApi.this, "PUT", REST_PATH, content, Void.class);
      this.selectedAnswer = com.google.api.client.util.Preconditions.checkNotNull(selectedAnswer, "Required parameter selectedAnswer must be specified.");
    }

    @Override
    public UpdatePollBean setAlt(java.lang.String alt) {
      return (UpdatePollBean) super.setAlt(alt);
    }

    @Override
    public UpdatePollBean setFields(java.lang.String fields) {
      return (UpdatePollBean) super.setFields(fields);
    }

    @Override
    public UpdatePollBean setKey(java.lang.String key) {
      return (UpdatePollBean) super.setKey(key);
    }

    @Override
    public UpdatePollBean setOauthToken(java.lang.String oauthToken) {
      return (UpdatePollBean) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdatePollBean setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdatePollBean) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdatePollBean setQuotaUser(java.lang.String quotaUser) {
      return (UpdatePollBean) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdatePollBean setUserIp(java.lang.String userIp) {
      return (UpdatePollBean) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String selectedAnswer;

    /**

     */
    public java.lang.String getSelectedAnswer() {
      return selectedAnswer;
    }

    public UpdatePollBean setSelectedAnswer(java.lang.String selectedAnswer) {
      this.selectedAnswer = selectedAnswer;
      return this;
    }

    @Override
    public UpdatePollBean set(String parameterName, Object value) {
      return (UpdatePollBean) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link PollBeanApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link PollBeanApi}. */
    @Override
    public PollBeanApi build() {
      return new PollBeanApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link PollBeanApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setPollBeanApiRequestInitializer(
        PollBeanApiRequestInitializer pollbeanapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(pollbeanapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
