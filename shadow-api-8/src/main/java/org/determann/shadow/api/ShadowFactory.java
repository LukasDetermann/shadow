package org.determann.shadow.api;

import org.determann.shadow.api.shadow.AnnotationUsage;
import org.determann.shadow.api.shadow.Shadow;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public interface ShadowFactory
{
   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #shadowFromType(TypeMirror)
    */
   public <SHADOW extends Shadow<? extends TypeMirror>> SHADOW shadowFromElement(@JdkApi Element element);

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #shadowFromElement(Element)
    */
   public <SHADOW extends Shadow<? extends TypeMirror>> SHADOW shadowFromType(@JdkApi TypeMirror typeMirror);

   public @UnmodifiableView List<AnnotationUsage> annotationUsage(@JdkApi List<? extends AnnotationMirror> annotationMirrors);
}
