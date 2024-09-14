package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.modifier.C_AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.C_Sealable;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

public interface C_Interface extends C_Declared,
                                     C_AbstractModifiable,
                                     C_StaticModifiable,
                                     C_Sealable,
                                     C_Erasable
{
}
