package olin.blaine.me.myapptest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT = 0, DELETE = 1;

    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    ImageView contactImg;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageUri = Uri.parse("android.resource://olin.blaine.mem.myapptest/drawable/no_user_logo.png");
    ContactRepository contactRepository;
    int longClickedItemIndex;
    ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.txtName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImg = (ImageView) findViewById(R.id.imgContact);
        contactRepository = new ContactRepository(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);


        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContacts);
        tabSpec.setIndicator("Contacts");
        tabHost.addTab(tabSpec);

        registerForContextMenu(contactListView);
        contactListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                longClickedItemIndex = position;
                return false;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),
                        nameTxt.getText().toString() + " was added to Contacts", Toast.LENGTH_SHORT).show();

                Contact contact = new Contact(contactRepository.getContactsCount(), nameTxt.getText().toString(), phoneTxt.getText().toString(),
                        emailTxt.getText().toString(), addressTxt.getText().toString(), imageUri);

                // This works for now, but it could be more efficient by not having another call to the DB - instead
                // adding to the list and then sorting it
                contactRepository.createContact(contact);
//                Contacts.add(contact);
                Contacts = contactRepository.GetAllContacts();
                adapter.notifyDataSetChanged();
            }
        });


        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addBtn.setEnabled(!String.valueOf(nameTxt.getText()).trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });

        Contacts = contactRepository.GetAllContacts();

        if(!Contacts.isEmpty())
            populateList();

    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Contact Options");
        menu.add(Menu.NONE, EDIT, menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, DELETE, menu.NONE, "Delete Contact");

    }

    public boolean onContextItemSelected(MenuItem item){

        switch(item.getItemId()){
            case EDIT:
                // TODO implement editing
                return true;
            case DELETE:
                contactRepository.deleteContact(Contacts.get(longClickedItemIndex));
                Contacts.remove(longClickedItemIndex);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }

    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK && reqCode == 1){
            contactImg.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }

    private void populateList(){
        adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter(){
            super(MainActivity.this, R.layout.tab_contact_list, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.tab_contact_list, parent, false);
            }
            Contact currentContact = Contacts.get(position);

            TextView name = (TextView) view.findViewById(R.id.nameDisplay);
            name.setText(currentContact.getName());

            TextView phone = (TextView) view.findViewById(R.id.phoneDisplay);
            phone.setText(currentContact.getPhoneNumber());

            TextView email = (TextView) view.findViewById(R.id.emailDisplay);
            email.setText(currentContact.getEmail());

            TextView address = (TextView) view.findViewById(R.id.addressDisplay);
            address.setText(currentContact.getAddress());

            ImageView contactImg = (ImageView) view.findViewById(R.id.contactImgDisplay);
            contactImg.setImageURI(currentContact.getImageUri());

            return view;
        }
    }


}
