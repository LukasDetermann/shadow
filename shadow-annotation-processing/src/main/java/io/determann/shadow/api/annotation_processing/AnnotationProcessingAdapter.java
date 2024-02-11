package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

public interface AnnotationProcessingAdapter
{
   static AnnotationMirror getMirror(AnnotationUsage annotationUsage)
   {
      return LangModelAdapter.getMirror(annotationUsage);
   }

   static DeclaredType getType(Declared declared)
   {
      return LangModelAdapter.getType(declared);
   }

   static ArrayType getType(Array array)
   {
      return LangModelAdapter.getType(array);
   }

   static ExecutableType getType(Executable executable)
   {
      return LangModelAdapter.getType(executable);
   }

   static TypeMirror getType(Shadow shadow)
   {
      return LangModelAdapter.getType(shadow);
   }

   static TypeVariable getType(Generic generic)
   {
      return LangModelAdapter.getType(generic);
   }

   static IntersectionType getType(Intersection intersection)
   {
      return LangModelAdapter.getType(intersection);
   }

   static NoType getType(io.determann.shadow.api.shadow.Module module)
   {
      return LangModelAdapter.getType(module);
   }

   static NullType getType(Null aNull)
   {
      return LangModelAdapter.getType(aNull);
   }

   static NoType getType(io.determann.shadow.api.shadow.Package aPackage)
   {
      return LangModelAdapter.getType(aPackage);
   }

   static PrimitiveType getType(Primitive primitive)
   {
      return LangModelAdapter.getType(primitive);
   }

   static NoType getType(Void aVoid)
   {
      return LangModelAdapter.getType(aVoid);
   }

   static WildcardType getType(Wildcard wildcard)
   {
      return LangModelAdapter.getType(wildcard);
   }

   static Element getElement(Annotationable annotationable)
   {
      return LangModelAdapter.getElement(annotationable);
   }

   static TypeElement getElement(Declared declared)
   {
      return LangModelAdapter.getElement(declared);
   }

   static ExecutableElement getElement(Executable executable)
   {
      return LangModelAdapter.getElement(executable);
   }

   static TypeParameterElement getElement(Generic generic)
   {
      return LangModelAdapter.getElement(generic);
   }

   static ModuleElement getElement(io.determann.shadow.api.shadow.Module module)
   {
      return LangModelAdapter.getElement(module);
   }

   static PackageElement getElement(Package aPackage)
   {
      return LangModelAdapter.getElement(aPackage);
   }

   static RecordComponentElement getElement(RecordComponent recordComponent)
   {
      return LangModelAdapter.getElement(recordComponent);
   }

   static VariableElement getElement(Variable<?> variable)
   {
      return LangModelAdapter.getElement(variable);
   }

   static Module getModule(LangModelContext context, Element element)
   {
      return LangModelAdapter.getModule(context, element);
   }

   static String getName(Element element)
   {
      return LangModelAdapter.getName(element);
   }

   static String getJavaDoc(LangModelContext context, Element element)
   {
      return LangModelAdapter.getJavaDoc(context, element);
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context, Element element)
   {
      return LangModelAdapter.getAnnotationUsages(context, element);
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(LangModelContext context, Element element)
   {
      return LangModelAdapter.getDirectAnnotationUsages(context, element);
   }

   static Set<io.determann.shadow.api.modifier.Modifier> getModifiers(Element element)
   {
      return LangModelAdapter.getModifiers(element);
   }

   static io.determann.shadow.api.modifier.Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return LangModelAdapter.mapModifier(modifier);
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #getShadow(LangModelContext, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, Element element)
   {
      return LangModelAdapter.getShadow(context, element);
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(LangModelContext, Element)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, TypeMirror typeMirror)
   {
      return LangModelAdapter.getShadow(context, typeMirror);
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return LangModelAdapter.getAnnotationUsages(context, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue getAnnotationValue(AnnotationValue annotationValue)
   {
      return LangModelAdapter.getAnnotationValue(annotationValue);
   }

   static Types getTypes(LangModelContext context)
   {
      return LangModelAdapter.getTypes(context);
   }

   static Elements getElements(LangModelContext context)
   {
      return LangModelAdapter.getElements(context);
   }
}
