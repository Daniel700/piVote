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
 * on 2015-08-16 at 18:43:41 UTC 
 * Modify at your own risk.
 */

package model.pollBeanApi.model;

/**
 * Model definition for AnswerBean.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the pollBeanApi. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class AnswerBean extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String answerText;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer answerVotes;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double percentage;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean selected;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getAnswerText() {
    return answerText;
  }

  /**
   * @param answerText answerText or {@code null} for none
   */
  public AnswerBean setAnswerText(java.lang.String answerText) {
    this.answerText = answerText;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getAnswerVotes() {
    return answerVotes;
  }

  /**
   * @param answerVotes answerVotes or {@code null} for none
   */
  public AnswerBean setAnswerVotes(java.lang.Integer answerVotes) {
    this.answerVotes = answerVotes;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getPercentage() {
    return percentage;
  }

  /**
   * @param percentage percentage or {@code null} for none
   */
  public AnswerBean setPercentage(java.lang.Double percentage) {
    this.percentage = percentage;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getSelected() {
    return selected;
  }

  /**
   * @param selected selected or {@code null} for none
   */
  public AnswerBean setSelected(java.lang.Boolean selected) {
    this.selected = selected;
    return this;
  }

  @Override
  public AnswerBean set(String fieldName, Object value) {
    return (AnswerBean) super.set(fieldName, value);
  }

  @Override
  public AnswerBean clone() {
    return (AnswerBean) super.clone();
  }

}
