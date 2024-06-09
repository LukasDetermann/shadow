package io.determann.shadow.api.shadow;

import io.determann.shadow.api.modifier.AbstractModifiable;
import io.determann.shadow.api.modifier.FinalModifiable;
import io.determann.shadow.api.modifier.Sealable;
import io.determann.shadow.api.modifier.StaticModifiable;

public interface Class extends Declared,
                               AbstractModifiable,
                               StaticModifiable,
                               Sealable,
                               FinalModifiable
{
}
