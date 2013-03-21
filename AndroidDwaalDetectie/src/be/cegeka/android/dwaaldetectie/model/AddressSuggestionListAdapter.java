package be.cegeka.android.dwaaldetectie.model;

import java.util.ArrayList;
import java.util.Collection;
import android.content.Context;
import android.widget.ArrayAdapter;


public class AddressSuggestionListAdapter extends ArrayAdapter<String>
{
	private ArrayList<String> adapter;


	public AddressSuggestionListAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
		adapter = new ArrayList<String>();
	}


	@Override
	public void add(String object)
	{
		adapter.add(object);
	}


	@Override
	public void addAll(Collection<? extends String> collection)
	{
		adapter.addAll(collection);
	}


	@Override
	public int getCount()
	{
		return adapter.size();
	}


	@Override
	public String getItem(int position)
	{
		return adapter.get(position);
	}


	@Override
	public void clear()
	{
		adapter.clear();
	}
}
