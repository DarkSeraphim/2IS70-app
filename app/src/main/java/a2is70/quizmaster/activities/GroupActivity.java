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
        dbi = AppContext.getInstance().getDBI();

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
        });

        recycler.addOnItemTouchListener(new GroupListener());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().getAccount().getType() == Account.Type.STUDENT){
                    //TODO open JoinGroupDialog.
                }   else {
                    //TODO open CreateGroupDialog.
                }
            }
        });
    }

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
            //TODO open EditGroupDialog for this group.
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        public void onRequestDisallowInterceptTouchEvent(boolean b){

        }
    }
}
