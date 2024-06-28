package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.shadow.modifier.AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.FinalModifiable;
import io.determann.shadow.api.shadow.modifier.Sealable;
import io.determann.shadow.api.shadow.modifier.StaticModifiable;

public interface Class extends Declared,
                               AbstractModifiable,
                               StaticModifiable,
                               Sealable,
                               FinalModifiable
{
}
