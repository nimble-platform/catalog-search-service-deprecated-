package eu.nimble.service.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Example
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-07T08:04:31.200Z")

public class Example   {
  @JsonProperty("prop1")
  private String prop1 = null;

  @JsonProperty("prop2")
  private Integer prop2 = null;

  public Example prop1(String prop1) {
    this.prop1 = prop1;
    return this;
  }

   /**
   * Get prop1
   * @return prop1
  **/
  @ApiModelProperty(value = "")
  public String getProp1() {
    return prop1;
  }

  public void setProp1(String prop1) {
    this.prop1 = prop1;
  }

  public Example prop2(Integer prop2) {
    this.prop2 = prop2;
    return this;
  }

   /**
   * Get prop2
   * @return prop2
  **/
  @ApiModelProperty(value = "")
  public Integer getProp2() {
    return prop2;
  }

  public void setProp2(Integer prop2) {
    this.prop2 = prop2;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Example example = (Example) o;
    return Objects.equals(this.prop1, example.prop1) &&
        Objects.equals(this.prop2, example.prop2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prop1, prop2);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Example {\n");
    
    sb.append("    prop1: ").append(toIndentedString(prop1)).append("\n");
    sb.append("    prop2: ").append(toIndentedString(prop2)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

