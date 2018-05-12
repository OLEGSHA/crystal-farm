package ru.windcorp.tge2.fileio;

import ru.windcorp.tge2.util.grh.Loadable;
import ru.windcorp.tge2.util.grh.Saveable;

/**
 * 
 * @author OLEGSHA
 *
 * @param <T>
 * @deprecated Use Loadable, Saveable individually
 */
@Deprecated
public interface ReverseSaveable<T> extends Loadable<T>, Saveable<T> {

}
