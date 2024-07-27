package io.determann.shadow.api.lang_model.shadow.directive;

import io.determann.shadow.api.shadow.directive.Directive;

/**
 * Relation between modules
 */
public sealed interface DirectiveLangModel extends Directive permits ExportsLangModel,
                                                                     OpensLangModel,
                                                                     ProvidesLangModel,
                                                                     RequiresLangModel,
                                                                     UsesLangModel
{
}
