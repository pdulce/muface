package muface.arch.command;

import org.springframework.data.domain.Pageable;

public interface IArqCommandPagination<R, P> {
    R execute(P params, Pageable pageable);

}