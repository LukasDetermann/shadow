package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.MethodLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ModuleLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.PackageLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.RecordComponentLangModel;
import io.determann.shadow.api.lang_model.shadow.type.RecordLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.lang_model.LangModelAdapter.*;
import static io.determann.shadow.api.shadow.Operations.NAMEABLE_GET_NAME;
import static io.determann.shadow.api.shadow.Operations.RECORD_COMPONENT_GET_TYPE;
import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public class RecordComponentImpl implements RecordComponentLangModel
{
   private final RecordComponentElement recordComponentElement;
   private final LangModelContext context;
   private final TypeMirror typeMirror;

   public RecordComponentImpl(LangModelContext context, RecordComponentElement recordComponentElement)
   {
      this.typeMirror = recordComponentElement.asType();
      this.context = context;
      this.recordComponentElement = recordComponentElement;
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return getTypes(getApi()).isSubtype(getMirror(), particularType((ShadowLangModel) shadow));
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return getTypes(getApi()).isAssignable(getMirror(), particularType((ShadowLangModel) shadow));
   }

   @Override
   public RecordLangModel getRecord()
   {
      return generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public ShadowLangModel getType()
   {
      return generalize(getApi(), getElement().asType());
   }

   @Override
   public MethodLangModel getGetter()
   {
      return (MethodLangModel) generalize(getApi(), getElement().getAccessor());
   }

   @Override
   public PackageLangModel getPackage()
   {
      return generalizePackage(getApi(), getElements(getApi()).getPackageOf(getElement()));
   }

   public RecordComponentElement getElement()
   {
      return recordComponentElement;
   }

   @Override
   public ModuleLangModel getModule()
   {
      return generalize(getApi(), getElements(getApi()).getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public List<AnnotationUsageLangModel> getAnnotationUsages()
   {
      return generalize(getApi(), getElements(getApi()).getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<AnnotationUsageLangModel> getDirectAnnotationUsages()
   {
      return generalize(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(), getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherRecordComponent, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }

   public TypeMirror getMirror()
   {
      return typeMirror;
   }

   public LangModelContext getApi()
   {
      return context;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
