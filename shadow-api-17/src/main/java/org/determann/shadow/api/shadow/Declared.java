package org.determann.shadow.api.shadow;

import org.determann.shadow.api.Annotationable;
import org.determann.shadow.api.NestingKind;
import org.determann.shadow.api.QualifiedNameable;
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.metadata.QualifiedName;
import org.determann.shadow.api.modifier.AccessModifiable;
import org.determann.shadow.api.modifier.StrictfpModifiable;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Set;

/**
 * Anything that can be a file. Can be converted into the following using {@link ShadowApi#convert(Declared)}
 * <ul>
 *    <li>{@link Annotation}</li>
 *    <li>{@link Class}</li>
 *    <li>{@link Enum}</li>
 *    <li>{@link Interface}</li>
 *    <li>{@link Record}</li>
 * </ul>
 */
public interface Declared extends Shadow<DeclaredType>,
                                  Annotationable<TypeElement>,
                                  AccessModifiable,
                                  StrictfpModifiable,
                                  QualifiedNameable<TypeElement>
{
   /**
    * returns true if this can be cast to that.
    * This can be useful if you want to check if a shadow implements for example a {@link java.util.Collection}
    * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
    */
   boolean isSubtypeOf(Shadow<? extends TypeMirror> shadow);

   /**
    * is it an outer or inner class? etc.
    */
   NestingKind getNesting();

   Field getFieldOrThrow(String simpleName);

   @UnmodifiableView List<Field> getFields();

   @UnmodifiableView List<Method> getMethods(String simpleName);

   @UnmodifiableView List<Method> getMethods();

   @UnmodifiableView List<Constructor> getConstructors();

   /**
    * returns the parentClass including interfaces
    */
   @UnmodifiableView List<Declared> getDirectSuperTypes();

   /**
    * returns all distinct supertypes including interfaces
    */
   @UnmodifiableView Set<Declared> getSuperTypes();

   @UnmodifiableView List<Interface> getInterfaces();

   Interface getInterfaceOrThrow(@QualifiedName String qualifiedName);

   @UnmodifiableView List<Interface> getDirectInterfaces();


   Interface getDirectInterfaceOrThrow(@QualifiedName String qualifiedName);

   Package getPackage();

   /**
    * part of the java language specification:
    * <p>
    * The binary name of a top level type is its canonical name. (qualified name)
    * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
    */
   String getBinaryName();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);
}
