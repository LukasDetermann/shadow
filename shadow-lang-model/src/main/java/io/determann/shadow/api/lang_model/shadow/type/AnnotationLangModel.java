package io.determann.shadow.api.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.shadow.modifier.StaticModifiableLangModel;
import io.determann.shadow.api.shadow.type.Annotation;

public interface AnnotationLangModel extends Annotation,
                                             DeclaredLangModel,
                                             StaticModifiableLangModel
{
}
