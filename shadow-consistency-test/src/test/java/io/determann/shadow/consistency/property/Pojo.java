package io.determann.shadow.consistency.property;

class Pojo
{
   private Long id;

   public Long getId() {return id;}

   public void setId(Long id) {this.id = id;}

   public boolean isValid() {return false;}

   public boolean getValid() {return false;}

   private java.util.List<String> values;

   public java.util.List<String> getValues() {return values;}

   public void setValues(java.util.List<String> values) {this.values = values;}

   private String sTrangeCase;

   public String getsTrangeCase() {return sTrangeCase;}

   public void setsTrangeCase(String sTrangeCase) {this.sTrangeCase = sTrangeCase;}

   private String NOT_A_CONSTANT;

   public String getNOT_A_CONSTANT() {return NOT_A_CONSTANT;}

   public void setNOT_A_CONSTANT(String NOT_A_CONSTANT) {this.NOT_A_CONSTANT = NOT_A_CONSTANT;}

   public void foo() {}
}
