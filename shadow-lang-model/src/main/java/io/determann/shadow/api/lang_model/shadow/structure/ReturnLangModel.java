package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.structure.Return;

public interface ReturnLangModel extends Return,
                                         AnnotationableLangModel
{
   ShadowLangModel getType();
}
