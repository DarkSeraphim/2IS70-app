package a2is70.quizmaster.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import a2is70.quizmaster.data.Account;
import a2is70.quizmaster.data.AppContext;
import a2is70.quizmaster.data.Group;
import java.util.List;
import java.util.ArrayList;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import a2is70.quizmaster.R;
import a2is70.quizmaster.database.DBInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView recycler;

    private FloatingActionButton addButton;

    private RecyclerView.Adapter<GroupHolder> adapter;

    private LayoutInflater inflater;

    private List<Group> groupList;

    private DBInterface dbi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        recycler = (RecyclerView) findViewById(R.id.rec_view);
        addButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        inflater = LayoutInflater.from(this);
        adapter = new GroupAdapter();
        recycler.setAdapter(adapter);
        //TODO: Uncomment Database connection.
        /*dbi = AppContext.getInstance().getDBI();

        //Pull group data from database.
        dbi.getGroups().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                groupList = response.body();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                //Stuff went wrong.
            }
        });*/

        //Dummy data.
        groupList = new ArrayList<Group>();
        groupList.add(new Group(-1, "Group1", "12345"));
        groupList.add(new Group(-2, "Group2", "23456"));
        groupList.add(new Group(-3, "Group3", "34567"));


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
        final LayoutInflater inf;
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit existing group.")
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface d, int which){
                        //TODO: Save Changes.
                    }})
                .setNegativeButton(("Discard"), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface d, int which){
                        d.dismiss();
                    }})
                .setView(R.layout.dialog_edit_group).create();
        //Populate fields with relevant information (group name, group access code).
        ((TextView)dialog.findViewById(R.id.edit_group_name)).setText(g.getName());
        ((TextView)dialog.findViewById(R.id.edit_group_access_code)).setText(g.getAccessCode());
        //Populate recyclerview with group data.
        rv = ((RecyclerView)dialog.findViewById(R.id.edit_group_recyclerview));
        inf = LayoutInflater.from(this);
        adapter = new RecyclerView.Adapter(){
            public void onBindViewHolder(RecyclerView.ViewHolder vh, int position){
                //Populate item_member with relevant data.
                ((TextView)vh.itemView.findViewById(R.id.member_name)).setText(((Account)g.getMembers().get(position)).getName());
                ((TextView)vh.itemView.findViewById(R.id.member_email)).setText(((Account)g.getMembers().get(position)).getEmail());
                vh.itemView.findViewById(R.id.member_image).setOnClickListener(
                        new View.OnClickListener(){
                            public void onClick(View v){
                                //TODO: Remove this member from the group.
                            }
                });
            }

            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup vg, int position){
                final View view = inf.inflate(R.layout.item_member, vg, false);
                return new GroupHolder(view);
            }

            public int getItemCount(){
                return g.getSize();
            }

        };
        rv.setAdapter(adapter);
    }

    /**
     * Dialog to join a new group. Only to be accessed by Students.
     */
    public void openJoinDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Join a new group.")
                .setPositiveButton("Join", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface d, int which){
                        //TODO: Uncomment database stuff.
                        /*dbi.joinGroup(((EditText)findViewById(R.id.join_group_access_code)).getText().toString())
                        .enqueue(new Callback<Group>() {
                            @Override
                            public void onResponse(Call<Group> call, Response<Group> response) {

                            }

                            @Override
                            public void onFailure(Call<Group> call, Throwable t) {

                            }
                        });*/
                        d.dismiss();
                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        d.dismiss();
                    }})
                .setView(R.layout.dialog_join_group).create();
    }

    /**
     * Dialog to create a new Group. Only to be accessed by Teachers.
     */
    public void openCreateDialog(){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create a new group.")
                .setPositiveButton("Join", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                    }})
                .setView(R.layout.dialog_create_group).create();
    }

    /**
     * Junk classes to satisfy who-ever made RecyclerViews.
     *
     */
    class GroupAdapter extends RecyclerView.Adapter<GroupHolder> {
        public void onBindViewHolder(GroupHolder gh, int position){
            Group g = groupList.get(position);
            ((TextView) gh.itemView.findViewById(R.id.group_name)).setText(g.getName());
            ((TextView) gh.itemView.findViewById(R.id.group_member_count)).setText(
                    g.getSize()==1? g.getSize()+" member.": g.getSize()+" members.");
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
            View position = rv.findChildViewUnder(e.getX(), e.getY());
            Group g = groupList.get(rv.getChildAdapterPosition(position));
            openEditDialog(g);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        public void onRequestDisallowInterceptTouchEvent(boolean b){

        }
    }
}
