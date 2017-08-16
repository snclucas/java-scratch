package imutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


public class ImmutableClass {
  /* All final fields */
  private final long id;
  private final String[] arrayOfStrings;
  private final Collection<String> collectionOfString;


  public ImmutableClass(final long id, final String[] arrayOfStrings,
                        final Collection<String> collectionOfString) {
    this.id = id;
    /* do not assign those fields directly from constructor arguments, make the copies instead.
    It will guarantee that state of the collection or array will not be changed from outside. */
    this.arrayOfStrings = Arrays.copyOf(arrayOfStrings, arrayOfStrings.length);
    this.collectionOfString = new ArrayList<>(collectionOfString);
  }


  public long getId() {
    return id;
  }


  /* provide the proper accessors (getters). For the collection, the immutable view should be exposed using
    Collections.unmodifiableXxx wrappers. */
  public Collection<String> getCollectionOfString() {
    return Collections.unmodifiableCollection( collectionOfString );
  }

  /* With arrays, the only way to ensure true immutability is to provide a copy instead of returning
  reference to the array. That might not be acceptable from a practical standpoint as it hugely depends
  on array size and may put a lot of pressure on garbage collector. */
  public String[] getArrayOfStrings() {
    return Arrays.copyOf( arrayOfStrings, arrayOfStrings.length );
  }


}
