package newspapercrud.domain.error;

import newspapercrud.common.Constantes;

public final class DuplicatedUsernameError extends DataBaseError {
    public DuplicatedUsernameError() {
        super(Constantes.DUPLICATED_USERNAME_ERROR);
    }
}
