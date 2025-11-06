package newspapercrud.domain.error;

import newspapercrud.common.Constantes;

public final class ForeignKeyError extends DataBaseError {
    public ForeignKeyError() {
        super(Constantes.FOREIGN_KEY_ERROR);
    }
}
