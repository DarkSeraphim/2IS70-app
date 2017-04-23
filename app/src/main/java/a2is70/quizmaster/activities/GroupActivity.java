package a2is70.quizmaster.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.gson.Gson;

import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import a2is70.quizmaster.R;
import a2is70.quizmaster.database.DBInterface;

import java.util.List;
import java.util.ArrayList;

import a2is70.quizmaster.utils.JsonConverter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//@todo member deletion
//@todo leave a group
//@todo popup when accescode already exists
public class GroupActivity extends AppCompatActivity {

    private RecyclerView recycler;

    private FloatingActionButton addButton;

    private RecyclerView.Adapter<GroupHolder> adapter;

    private LayoutInflater inflater;

    private List<Group> groupList = new ArrayList<>();

    private DBInterface dbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        recycler = (RecyclerView) findViewById(R.id.group_recycler_view);
        addButton = (FloatingActionButton) findViewById(R.id.group_add_button);
        inflater = LayoutInflater.from(this);
        adapter = new GroupAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        dbi = AppContext.getInstance().getDBI();

        //Pull group data from database.
        loadGroups();

        recycler.addOnItemTouchListener(new GroupListener());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().getAccount().getType() == Account.Type.STUDENT){
                    //User is STUDENT.
                    openJoinDialog();
                }   else {
                    //User is TEACHER.
                    openCreateDialog();
                }
            }
        });
    }

    /**
     * Dialog to edit a currently existing group.
     * E.g. change name, view access code, kick member.
     * @param g Group which is being edited.
     */
    public void openEditDialog(final Group g){
        final RecyclerView rv;
        final RecyclerView.Adapter adapter;
        //final View view = inflater.inflate(R.layout.dialog_edit_group, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit existing group.")
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface d, int which){
                        //Currently editing name/code is not implemented.
                    }})
                .setNegativeButton(("Discard"), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface d, int which){
                        d.dismiss();
                    }})
                .setView(R.layout.dialog_edit_group)
                .create();
        //Populate fields with relevant information (group name, group access code).
        //@todo fix the null errors
        EditText groupName = (EditText) dialog.findViewById(R.id.edit_group_groupname);
        EditText groupCode = (EditText) dialog.findViewById(R.id.edit_group_accesscode);

        groupName.setText(g.getName());
        groupCode.setText(g.getAccessCode());
        //These values can not be changed by the user (not implemented).
        groupName.setFocusable(false);
        groupCode.setFocusable(false);

        //Populate recyclerview with group data.
        rv = ((RecyclerView)dialog.findViewById(R.id.edit_group_recyclerview));
        adapter = new RecyclerView.Adapter(){
            public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position){
                //Populate item_member with relevant data.
                ((TextView)vh.itemView.findViewById(R.id.member_name)).setText(((Account)g.getMembers().get(position)).getName());
                ((TextView)vh.itemView.findViewById(R.id.member_email)).setText(((Account)g.getMembers().get(position)).getEmail());
            }

            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup vg, int position){
                final View view = inflater.inflate(R.layout.item_member, vg, false);
                return new GroupHolder(view);
            }

            public int getItemCount(){
                return g.getSize();
            }

        };
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    final View position = rv.findChildViewUnder(e.getX(), e.getY());
                    final Account a;
                    try {
                        a = (Account) g.getMembers().get(rv.getChildAdapterPosition(position));
                    } catch (IndexOutOfBoundsException ex) {
                        //User clicked non-existent element.
                        return false;
                    }
                    final AlertDialog confirm = new AlertDialog.Builder(GroupActivity.this)
                            .setTitle("Really kick member "+a.getName()+"?")
                            .setPositiveButton("Kick", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbi.kickMember(g.getId(), a.getId()).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            g.getMembers().remove(a);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    confirm.show();
                }
                    return false;
            }

            public void onTouchEvent(RecyclerView rv, MotionEvent e){

            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        dialog.findViewById(R.id.dialog_edit_delete).setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Delete this group from the list and database.
                final AlertDialog confirm = new AlertDialog.Builder(GroupActivity.this)
                        .setTitle("Confirm deleting Group.")
                        .setMessage("Really delete this group?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                //Actually delete it.
                                dbi.deleteGroup(g.getId()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        loadGroups();
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                                dialog.dismiss();
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                d.cancel();
                            }})
                        .create();
                confirm.show();
            }
        });
        dialog.show();
    }

    /**
     * Dialog to join a new group. Only to be accessed by Students.
     */
    public void openJoinDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Join a new group.")
                .setPositiveButton("Join", null)
                .setNegativeButton("Cancel", null)
                .setView(R.layout.dialog_join_group)
                .create();
        dialog.setButton(dialog.BUTTON_POSITIVE, "Join", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int which){
                dbi.joinGroup(((EditText)dialog.findViewById(R.id.join_group_access_code)).getText().toString())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                loadGroups();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                final AlertDialog fail = new AlertDialog.Builder(GroupActivity.this)
                                        .setMessage("Could not join group with this access code.").create();
                                fail.show();
                                Log.d("GroupActivity", "JoinGroup error: " + t);
                            }
                        });
                d.cancel();
            }});
        dialog.setButton(dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                d.cancel();
            }});
        dialog.show();
    }

    /**
     * Dialog to create a new Group. Only to be accessed by Teachers.
     */
    public void openCreateDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create a new group.")
                .setPositiveButton("Create", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(final DialogInterface dialog, int which){
                        String name = ((EditText)((AlertDialog) dialog).findViewById(R.id.create_group_name)).getText().toString();
                        String code = ((EditText)((AlertDialog) dialog).findViewById(R.id.create_group_code)).getText().toString();
                        if (name.equals("") || code.equals("")){
                            //If we want to do input checking.
                        }
                        Log.d("Groups", new Gson().toJson(new Group(-1, name, code)));
                        dbi.createGroup(new Group(-1, name, code)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                //reload the group list (hopefully with newly added list).
                                Log.d("Groups", "Create status: " + response.code());
                                loadGroups();
                                dialog.cancel();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //Stuff went wrong.
                                Log.e("Groups", "Add error: ", t);
                            }
                        });
                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }})
                .setView(R.layout.dialog_create_group).create();
        dialog.show();
    }

    /**Method to (re-)load groups from database and display them in the list.*/
    public void loadGroups(){
        dbi.getGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                Log.d("Groups", "Status: " + response.code());
                groupList.clear();
                groupList.addAll(response.body());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                //Stuff went wrong.
                Log.e("Groups", "Exception: ", t);
            }
        });
    }

    // Either onKeyDown or onOptionsItemSelected is fired
    // I have no clue
    // But this overrides the back button, and throws back the groups
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Do something here
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, OverviewActivity.class);
        intent.putExtra("groups", JsonConverter.toJson(groupList));
        startActivity(intent);
        finish();
    }

    /**
     * Junk classes to satisfy who-ever made RecyclerViews.
     *
     */
    class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {
        public void onBindViewHolder(GroupHolder gh, int position){
            //Sets data for one RecyclerView element at index @param position.
            Group g = groupList.get(position);
            ((TextView) gh.itemView.findViewById(R.id.group_name)).setText(g.getName());
            ((TextView) gh.itemView.findViewById(R.id.group_member_count)).setText(
                    g.getSize()==1 ? g.getSize()+" member" : g.getSize()+" members");
        }

        public int getItemCount(){
            return groupList.size();
        }

        public GroupHolder onCreateViewHolder(ViewGroup v, int position){
            View view = inflater.inflate(R.layout.item_group, v, false);
            return new GroupHolder(view);
        }
    }

    class GroupHolder extends RecyclerView.ViewHolder {
        public GroupHolder(View view){
            super(view);
        }
    }

    class GroupListener implements RecyclerView.OnItemTouchListener{

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                View position = rv.findChildViewUnder(e.getX(), e.getY());
                Group g;
                try {
                    g = groupList.get(rv.getChildAdapterPosition(position));
                } catch (ArrayIndexOutOfBoundsException ex) {
                    //user clicked on non-existent view element.
                    return false;
                }
                if (AppContext.getInstance().getAccount().getType() == Account.Type.TEACHER) {
                    //User is a TEACHER.
                    openEditDialog(g);
                } else {
                    //User is a STUDENT.
                    final AlertDialog leave = new AlertDialog.Builder(GroupActivity.this)
                            .setTitle("Leave Group")
                            .setMessage("Leave this Group?")
                            .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface d, int which) {
                                    dbi.leaveGroup(AppContext.getInstance().getAccount().getId()).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            d.cancel();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            //You can never leave.
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface d, int which) {
                                    d.cancel();
                                }
                            })
                            .create();
                    leave.show();
                }
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        public void onRequestDisallowInterceptTouchEvent(boolean b){

        }
    }
}
