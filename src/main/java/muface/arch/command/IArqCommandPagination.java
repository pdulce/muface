package muface.arch.command;

import org.springframework.data.domain.Pageable;

public interface IArqCommandPagination<R, P> {
    R executeInner(P params, Pageable pageable);

}