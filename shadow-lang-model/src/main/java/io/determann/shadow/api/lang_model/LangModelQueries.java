package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.lang_model.shadow.*;
import io.determann.shadow.api.lang_model.shadow.directive.*;
import io.determann.shadow.api.lang_model.shadow.modifier.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.directive.*;
import io.determann.shadow.api.shadow.modifier.*;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.*;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import java.util.Objects;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;
import static java.util.Objects.requireNonNull;

public interface LangModelQueries
{
   public static NameableLangModel query(Nameable nameable)
   {
      return ((NameableLangModel) validate(nameable));
   }

   public static QualifiedNameableLamgModel query(QualifiedNameable qualifiedNameable)
   {
      return ((QualifiedNameableLamgModel) validate(qualifiedNameable));
   }

   public static WildcardLangModel query(Wildcard wildcard)
   {
      return ((WildcardLangModel) validate(wildcard));
   }

   public static PrimitiveLangModel query(Primitive primitive)
   {
      return ((PrimitiveLangModel) validate(primitive));
   }

   public static ShadowLangModel query(Shadow shadow)
   {
      return ((ShadowLangModel) validate(shadow));
   }

   public static PackageLangModel query(Package aPackage)
   {
      return (PackageLangModel) validate(aPackage);
   }

   public static ModuleEnclosedLangModel query(ModuleEnclosed moduleEnclosed)
   {
      return ((ModuleEnclosedLangModel) validate(moduleEnclosed));
   }

   public static DocumentedLangModel query(Documented documented)
   {
      return (DocumentedLangModel) validate(documented);
   }

   public static DeclaredLangModel query(Declared declared)
   {
      return (DeclaredLangModel) validate(declared);
   }

   public static EnumLangModel query(Enum anEnum)
   {
      return (EnumLangModel) validate(anEnum);
   }

   public static InterfaceLangModel query(Interface anInterface)
   {
      return (InterfaceLangModel) validate(anInterface);
   }

   public static RecordLangModel query(Record record)
   {
      return (RecordLangModel) validate(record);
   }

   public static ClassLangModel query(Class aClass)
   {
      return (ClassLangModel) validate(aClass);
   }

   public static ArrayLangModel query(Array array)
   {
      return (ArrayLangModel) validate(array);
   }

   public static EnumConstantLangModel query(EnumConstant enumConstant)
   {
      return (EnumConstantLangModel) validate(enumConstant);
   }

   public static FieldLangModel query(Field field)
   {
      return (FieldLangModel) validate(field);
   }

   public static ParameterLangModel query(Parameter parameter)
   {
      return (ParameterLangModel) validate(parameter);
   }

   public static VariableLangModel query(Variable variable)
   {
      return (VariableLangModel) validate(variable);
   }

   public static ConstructorLangModel query(Constructor constructor)
   {
      return (ConstructorLangModel) validate(constructor);
   }

   public static ExecutableLangModel query(Executable executable)
   {
      return (ExecutableLangModel) validate(executable);
   }

   public static MethodLangMethod query(Method method)
   {
      return (MethodLangMethod) validate(method);
   }

   public static RecordComponentLangModel query(RecordComponent recordComponent)
   {
      return (RecordComponentLangModel) validate(recordComponent);
   }

   public static ReturnLangModel query(Return aReturn)
   {
      return (ReturnLangModel) validate(aReturn);
   }

   public static ReceiverLangModel query(Receiver receiver)
   {
      return (ReceiverLangModel) validate(receiver);
   }

   public static IntersectionLangModel query(Intersection intersection)
   {
      return (IntersectionLangModel) validate(intersection);
   }

   public static GenericLangModel query(Generic generic)
   {
      return (GenericLangModel) validate(generic);
   }

   public static AnnotationUsageLangModel query(AnnotationUsage annotationUsage)
   {
      return (AnnotationUsageLangModel) validate(annotationUsage);
   }

   public static ModuleLangModel query(Module module)
   {
      return (ModuleLangModel) validate(module);
   }

   public static AnnotationLangModel query(Annotation annotation)
   {
      return (AnnotationLangModel) validate(annotation);
   }

   public static ModifiableLangModel query(Modifiable modifiable)
   {
      return (ModifiableLangModel) validate(modifiable);
   }

   public static AbstractModifiableLangModel query(AbstractModifiable abstractModifiable)
   {
      return (AbstractModifiableLangModel) validate(abstractModifiable);
   }

   public static AccessModifiableLangModel query(AccessModifiable accessModifiable)
   {
      return (AccessModifiableLangModel) validate(accessModifiable);
   }

   public static DefaultModifiableLangModel query(DefaultModifiable defaultModifiable)
   {
      return (DefaultModifiableLangModel) validate(defaultModifiable);
   }

   public static FinalModifiableLangModel query(FinalModifiable finalModifiable)
   {
      return (FinalModifiableLangModel) validate(finalModifiable);
   }

   public static NativeModifiableLangModel query(NativeModifiable nativeModifiable)
   {
      return (NativeModifiableLangModel) validate(nativeModifiable);
   }

   public static SealableLangModel query(Sealable sealable)
   {
      return (SealableLangModel) validate(sealable);
   }

   public static StaticModifiableLangModel query(StaticModifiable staticModifiable)
   {
      return (StaticModifiableLangModel) validate(staticModifiable);
   }

   public static StrictfpModifiableLangModel query(StrictfpModifiable strictfpModifiable)
   {
      return (StrictfpModifiableLangModel) validate(strictfpModifiable);
   }

   public static ExportsLangModel query(Exports exports)
   {
      return (ExportsLangModel) validate(exports);
   }

   public static OpensLangModel query(Opens opens)
   {
      return (OpensLangModel) validate(opens);
   }

   public static ProvidesLangModel query(Provides provides)
   {
      return (ProvidesLangModel) validate(provides);
   }

   public static RequiresLangModel query(Requires requires)
   {
      return (RequiresLangModel) validate(requires);
   }

   public static UsesLangModel query(Uses uses)
   {
      return (UsesLangModel) validate(uses);
   }

   private static <T extends ImplementationDefined> T validate(T toValidate)
   {
      if (!Objects.equals(requireNonNull(toValidate.getImplementationName()), IMPLEMENTATION_NAME))
      {
         throw new IllegalArgumentException("Tried to use \"" + IMPLEMENTATION_NAME + " \" to query \"" + toValidate + "\" based on \"" + toValidate.getImplementationName() + "\"");
      }
      return toValidate;
   }
}
