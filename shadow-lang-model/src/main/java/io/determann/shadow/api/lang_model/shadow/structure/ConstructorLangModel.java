package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.modifier.AccessModifiableLangModel;
import io.determann.shadow.api.shadow.structure.Constructor;

public interface ConstructorLangModel extends Constructor,
                                              ExecutableLangModel,
                                              AccessModifiableLangModel
{
}
