## Dlaczego przy SeekBar nie dało się użyć lambdy, a przy CheckBox tak?

Interfejs `SeekBar.OnSeekBarChangeListener` zawiera **trzy metody abstrakcyjne**:
- `onProgressChanged()`
- `onStartTrackingTouch()`
- `onStopTrackingTouch()`

Ponieważ interfejs ma więcej niż jedną metodę, nie jest to interfejs SAM (Single Abstract Method).  
W Javie lambdy działają tylko z interfejsami posiadającymi dokładnie jedną metodę abstrakcyjną.  
Dlatego przy SeekBar trzeba użyć pełnej klasy anonimowej:

```java
sbLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { ... }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
});