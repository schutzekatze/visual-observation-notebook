package petnicameteorgroup.meteorobservationnotebook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vladi on 11/2/2017.
 */

public class NoteFragment extends Fragment {

    public static final String NOTE_BITMAP_ARG = "petnicameteorgroup.meteorobservationnotebook.NoteFragment.NOTE_BITMAP_ARG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        Bundle args = getArguments();
        Bitmap bitmap = args.getParcelable(NOTE_BITMAP_ARG);

        ImageView imageView = rootView.findViewById(R.id.note_image_view);
        imageView.setImageBitmap(bitmap);

        return rootView;
    }
}
