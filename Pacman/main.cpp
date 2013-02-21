#include <allegro.h>
#include<stdlib.h>
#include<fstream>
/* GLOBAL DEFINES */
#define  DEGREES(x) int((x)/360.0*0xFFFFFF)
#define OBJ 500
#define max(a, b) (((a) > (b)) ? (a): (b))
#define min(a, b) (((a) < (b)) ? (a): (b))
#define UP 0
#define DOWN 1
#define LEFT 2 
#define RIGHT 3
/* GLOBAL DEFINES */

/* TYPE DEFINES */
typedef int cordinates;
/* TYPE DEFINES */
/*GLOBAL DECLARATIONS */
BITMAP *carh;
BITMAP *car1;
BITMAP *car2;
BITMAP *active_page;
BITMAP *page1;
BITMAP *page2;
BITMAP *fsprite;
BITMAP *bigs;
BITMAP *enemy;
BITMAP *enemy1;
BITMAP *enemy2;
BITMAP *enemy3;
BITMAP *end;
BITMAP *lives1;
BITMAP *score1;
BITMAP *name;
BITMAP *blank;
BITMAP *won;
BITMAP *b_food;
BITMAP *b_food1;
BITMAP *b_food2;
BITMAP *b_food3;
BITMAP *title;
BITMAP *last_page;
BITMAP *scoree;
BITMAP *hscore;
BITMAP *blue;
BITMAP *finished;
BITMAP *blank_dot;
SAMPLE *sound;
SAMPLE *sound1;

int x=395;
int y=420;
int n=1;
int ex=700;
int ey=40;
int ex1=50;
int ey1=50;
int ex2=50;
int ey2=515;
int ex3=700;
int ey3=520;
int frame=0;
double fps=0;
int counter=0;
int check=0;
int edx=0,edy=-3;
int edx1=7,edy1=0;
int edx2=7,edy2=0;
int edx3=-7,edy3=0;
int score=0;
int lives=3;
int count=0;
int flage=0;
int flage1=0;
int flage2=0;
int flage3=0;
bool checktimer=false;
char pname[30]={};
int score2=0;
int finish=0;
cordinates fx[]={   85,105,125,145,165,195,225,245,265,285,305,325,345,435,455,475,495,515,535,555,585,615,635,655,675,695,
65,195,350,427,585,715,
65,85,105,125,145,165,195,225,245,270,305,325,355,385,405,430,455,475,505,535,555,585,615,635,655,675,695,715,
65,195,270,505,585,715,
65,85,105,125,145,165,195,270,305,325,350,435,475,505,585,615,635,655,675,695,715,
195,350,435,585,
195,270,305,325,350,385,405,435,475,505,585,
65,85,105,125,145,165,195,225,245,270,505,535,555,585,615,635,655,675,695,715,
195,270,505,585,
195,270,305,325,345,365,385,405,435,455,475,505,585,
195,270,505,585,
65,85,105,125,145,165,195,225,245,270,305,345,435,455,475,505,535,555,585,615,635,655,675,695,715,
65,195,345,435,585,715,
65,85,115,195,225,245,275,305,320,345,378,405,435,455,475,505,535,555,585,665,695,715,
115,195,275,505,585,665,
65,85,115,145,165,195,275,305,325,350,427,455,475,505,585,615,635,665,695,715,
   85,105,125,145,165,185,205,225,245,265,285,305,325,350,385,400,428,455,475,495,515,535,555,585,615,635,655,675,695,
65,350,427,715};

        
cordinates fy[]={   45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,45,
77,77,77,77,77,77,
109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,109,
141,141,141,141,141,141,
173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,173,
195,195,195,195,
217,217,217,217,217,217,217,217,217,217,217,
265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,265,
290,290,290,290,
320,320,320,320,320,320,320,320,320,320,320,320,320,
345,345,345,345,
375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,375,
400,400,400,400,400,400,
430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,430,
450,450,450,450,450,450,
475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,475,
    525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,525,
500,500,500,500};



bool DirDisable[4]={};
/*GLOBAL DECLARATIONS */
/* STRUCT DECLARATIONS*/
typedef struct FOOD
{
     short int xmin1;
     short int ymin1;
     short int xmin2;
     short int ymin2 ;
     short int xmax1; 
     short int ymax1;
     short int xmax2 ;
     short int ymax2;
     bool disabled;
       
       
};
FOOD levelone[OBJ];
/* STRUCT DECLARATIONS*/


/*FUNCTION DECLARATIONS */
void init();
void deinit();
void carhandle();
void spritecontrol(int n);
void input();
void processing();
void output();
void loading();
void collision(FOOD &object);
void _Food_control();
void _get_cordinates();
void _collision_check();
int test_collide_enemy( int ex , int ey);
int test_collide_carh(int x , int y);
int test_collide_enemy1( int ex1 , int ey1);
int test_collide_enemy2( int ex2 , int ey2);
int test_collide_enemy3( int ex3 , int ey3);
int death_collide(int ex,int ey,int px, int py);
void file_handling(void);
void bonus_food(int exx,int eyy,int px, int py);
void kill_enemy(int exx,int eyy,int px, int py);
void kill_enemy1(int exx,int eyy,int px, int py);
void kill_enemy2(int exx,int eyy,int px, int py);
void kill_enemy3(int exx,int eyy,int px, int py);
void file_handling(void);
void sorting(void);
/*FUNCTION DECLARATIONS */

/*TIMER FUNCTIONS */
volatile int z;
void t()
{
     z++;
 }
 END_OF_FUNCTION(t);
 volatile int timer;
 void mytimer()
 {
      timer++;
  }
  END_OF_FUNCTION(mytimer);



/*TIMER FUNCTIONS */


/*MAIN START */
int main()
{

        init();   
        loading();
     
              draw_sprite(screen,title,0,70);
                          
       int i=0;   
              textprintf_ex(screen, font, 100, 540, makecol(255, 255, 255),1,"Enter your name: ", pname);  
                    
              
  while( i <30  && !key[KEY_ENTER] )
       {
                 
       pname[i]=readkey()%256;
       if ( pname[i] == char(8))
       {
            pname[i]=' ';
            pname[i-1]=' ';
             i-=2;
       
       }
              textprintf_ex(screen, font, 300, 540, makecol(255, 255, 255),1,"%s", pname);          
              i++;
              
       }

              
              
              
                           rest(300);   
                           play_sample(sound,200, 128,1000, 1);  
              
              
              
              
              
       while (!key[KEY_ESC])
    {
          
         
                          
                
                
                
                
          
          input();
          processing();
          draw_sprite(active_page,blank_dot,0,0);
          output();
          sorting();                      
          textprintf_ex(screen, font, 175, 580, makecol(255, 255, 255),-1, " %d", score);   
          
    }
    key[KEY_ENTER] = false;                
             
                    //end main loop 
     
     

    deinit(); 
    return 0;
}

END_OF_MAIN()
/*MAIN END */
void init()
{
    int depth, res;
    allegro_init();
    depth = desktop_color_depth();
    if (depth == 0) depth = 32;
    set_color_depth(depth);
    res = set_gfx_mode(GFX_AUTODETECT, 800, 600, 0, 0);
    if (res != 0)
    {
        allegro_message(allegro_error);
        exit(-1);
    }

    install_timer();
    install_keyboard();
    install_mouse();
    install_sound(DIGI_AUTODETECT, MIDI_AUTODETECT, 0) ;
    
    LOCK_VARIABLE(z);
    LOCK_FUNCTION(t);
    LOCK_VARIABLE(time);
    LOCK_FUNCTION(mytimer);

    install_int_ex(t,10000);
    install_int_ex(mytimer,BPS_TO_TIMER(1));
}

void deinit()
{
     destroy_bitmap(car1);
     destroy_bitmap(car2);
     destroy_bitmap(fsprite);
     destroy_bitmap(enemy);    
     destroy_bitmap(enemy1);
     destroy_bitmap(enemy2);
     destroy_bitmap(enemy3);
     destroy_bitmap(score1);
     destroy_bitmap(lives1);
     destroy_bitmap(name);
     destroy_bitmap(b_food);
     destroy_bitmap(b_food1);
     destroy_bitmap(b_food2);
     destroy_bitmap(b_food3);
     destroy_bitmap(finished);
     destroy_bitmap(blue);
     destroy_bitmap(hscore);
     destroy_bitmap(blank_dot);
     set_gfx_mode(GFX_TEXT,80, 25, 0, 0);
     clear_keybuf();
     allegro_exit();

    /* add other deinitializations here */
}
void carhandle()
{
         if ( carh==car1)
     {
         carh=car2;
     }
     else
     carh=car1;    
 }
 
 void spritecontrol(int n)
 {
      draw_sprite(active_page,b_food,65,523);
      draw_sprite(active_page,b_food1,715,525);
         draw_sprite(active_page,b_food2,65,45);
         draw_sprite(active_page,b_food3,715,45);
     
      switch(n)
      {
               case 1:
                draw_sprite(active_page,carh,x,y);
                break;
                case 2:
                draw_sprite_h_flip(active_page, carh, x, y);
                
                break;     
                case 3:
                rotate_sprite(active_page, carh, x,y, DEGREES(90));
                
                break;
                case 4:
                rotate_sprite(active_page, carh, x,y, DEGREES(-90));
                
                break;     
               }

      }

void input()
{
      clear_to_color(active_page,makecol(220,150,90));
     //  draw_sprite(screen,title,0,70);

      masked_blit(bigs, active_page, 0,0,0,0, bigs->w,bigs->h);



     
     //enemy   
      
              if (test_collide_enemy(ex,ey)==1) 
                            {
                            
                                if(edy<0)
                                {  // textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "First");                    
                                     edy=0;
                                     ey=ey+7;
                                     if(rand()%2==0)
                                     {      edx=-7;        }
                                     else
                                            edx=7;   }
                               else if(edy>0 )
                                  {  
                                   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Second");                    
                                     edy=0;
                                     ey=ey-7;
                                     if(rand()%2==0)
                                     {      edx=-7;        }
                                     else
                                            edx=7;     }
                                else if(edx<0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Third");                    
                                     edx=0;
                                     ex=ex+7;
                                     if(rand()%2==0)
                                     {      edy=-7;        }
                                     else
                                            edy=7;      }
                               else if(edx>0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),4, "Fourth");                    
                                     edx=0;
                                     ex=ex-10;
                                     if(rand()%2==0)
                                     {      edy=-7;        }
                                     else
                                            edy=7;     }

                            }


 
 
       ex=ex+edx;
       ey=ey+edy;
 if (timer>8|| checktimer==false && flage==0)
    {   death_collide(ex,ey,x,y);      
    }  

      
           
    //enemy1-----------------------------------------       
          if (test_collide_enemy1(ex1,ey1)==1) 
                            {
                            
                                if(edy1<0)
                                {   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "First");                    
                                     edy1=0;
                                     ey1=ey1+7;
                                     if(rand()%2==0)
                                     {      edx1=-7;        }
                                     else
                                            edx1=7;   }
                               else if(edy1>0 )
                                  {  
                                   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Second");                    
                                     edy1=0;
                                     ey1=ey1-7;
                                     if(rand()%2==0)
                                     {      edx1=-7;        }
                                     else
                                            edx1=7;     }
                                else if(edx1<0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Third");                    
                                     edx1=0;
                                     ex1=ex1+7;
                                     if(rand()%2==0)
                                     {      edy1=-7;        }
                                     else
                                            edy1=7;      }
                               else if(edx1>0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),4, "Fourth");                    
                                     edx1=0;
                                     ex1=ex1-7;
                                     if(rand()%2==0)
                                     {      edy1=-7;        }
                                     else
                                            edy1=7;     }

                            }


 
 
       ex1=ex1+edx1;
       ey1=ey1+edy1;
       if (timer>8|| checktimer==false && flage1==0)
       {
        death_collide(ex1,ey1,x,y);                          
}

     
    //enemy2--------------------------------------
    
          if (test_collide_enemy2(ex2,ey2)==1) 
                            {
                            
                                if(edy2<0)
                                {   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "First");                    
                                     edy2=0;
                                     ey2=ey2+7;
                                     if(rand()%2==0)
                                     {      edx2=-7;        }
                                     else
                                            edx2=7;   }
                               else if(edy2>0 )
                                  {  
                                   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Second");                    
                                     edy2=0;
                                     ey2=ey2-7;
                                     if(rand()%2==0)
                                     {      edx2=-7;        }
                                     else
                                            edx2=7;     }
                                else if(edx2<0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Third");                    
                                     edx2=0;
                                     ex2=ex2+7;
                                     if(rand()%2==0)
                                     {      edy2=-7;        }
                                     else
                                            edy2=7;      }
                               else if(edx2>0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),4, "Fourth");                    
                                     edx2=0;
                                     ex2=ex2-7;
                                     if(rand()%2==0)
                                     {      edy2=-7;        }
                                     else
                                            edy2=7;     }

                            }


 
 
       ex2=ex2+edx2;
       ey2=ey2+edy2;
       if (timer>8|| checktimer==false && flage2==0)
       {
       death_collide(ex2,ey2,x,y);
}
      
    //enemy3------------------------------------ 
           if (test_collide_enemy3(ex3,ey3)==1) 
                            {
                            
                                if(edy3<0)
                                {  // textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "First");                    
                                     edy3=0;
                                     ey3=ey3+5;
                                     if(rand()%2==0)
                                     {      edx3=-5;        }
                                     else
                                            edx3=5;   }
                               else if(edy3>0 )
                                  {  
                                   //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Second");                    
                                     edy3=0;
                                     ey3=ey3-5;
                                     if(rand()%2==0)
                                     {      edx3=-5;        }
                                     else
                                            edx3=5;     }
                                else if(edx3<0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),1, "Third");                    
                                     edx3=0;
                                     ex3=ex3+5;
                                     if(rand()%2==0)
                                     {      edy3=-5;        }
                                     else
                                            edy3=5;      }
                               else if(edx3>0 )
                                {    
                                     //textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),4, "Fourth");                    
                                     edx3=0;
                                     ex3=ex3-5;
                                     if(rand()%2==0)
                                     {      edy3=-5;        }
                                     else
                                            edy3=5;     }

                            }


 
 
       ex3=ex3+edx3;
       ey3=ey3+edy3;
       if (timer>8|| checktimer==false && flage3==0)
{
                            death_collide(ex3,ey3,x,y);
}
                     bonus_food(65,525,x,y);
                     bonus_food(715,525,x,y);
                     bonus_food(65,45,x,y);
                     bonus_food(715,45,x,y);          

textprintf_ex(active_page, font, 660, 580, makecol(255, 255, 255),-1, " %d", lives);
     
     
 //---------------------------------------- 
          if ( key[KEY_LEFT] && DirDisable[LEFT]== false )
          {
               x-=5;
               if(x<=20 && y >=250 && y<=290)
                        {
                                   x=770;
                                                         }
               DirDisable[UP]=true;
               DirDisable[DOWN]=true;
               if(test_collide_carh(x,y)==1)
                
                        {          x=x+5;
                                   DirDisable[LEFT]=true;
                                                         }
            
            carhandle();
            n=2;            
                              
           }
        else if ( key[KEY_RIGHT]  && DirDisable[RIGHT]== false  )
          {

               x+=5;
                        if(x>=770 && y >=250 && y<=290)
                        {
                                 x=30;
                        } 
      
          
                        if ( test_collide_carh(x,y)==1)
                        {        x=x-5;
                                 DirDisable[RIGHT]=true;
                        }
                        carhandle();
                        n=1;
           }
        else if ( key[KEY_UP] && DirDisable[UP]== false )
        {

               y-=5;
         
        
               if ( test_collide_carh(x,y)==1)
               {        y=y+5;
                        DirDisable[UP]=true;
               }
               carhandle();
               n=4;
         }
        else if ( key[KEY_DOWN]  && DirDisable[DOWN]== false  )
        {
               y+=5;
      
 
               if (test_collide_carh(x,y)==1)
               {           y=y-5;
                           DirDisable[DOWN]=true;
               }
               carhandle();
               n=3;
              
        }
         
   
    
           {
                for ( counter =0 ; counter <4 ; counter ++)
                DirDisable[counter]=false;
           }
           
     
 }
void processing()
{
              spritecontrol(n);           
              _get_cordinates();
              _collision_check();
              _Food_control();  
               
              frame++;
              if (z >= 100)
                 {
             	    fps = (100. * frame) / z;
             	    z = 0;
                    frame = 0;
                 }
             // vsync();
              //vsync();
              vsync();
              vsync();              
              vsync();  
     
 }
void output ()
{
  //   textprintf_ex(active_page, font, 120, 40, makecol(255, 255, 255),-1, "fps %f %d %d", fps,z, _color_collide(0,0,bigs,x,y,carh));

show_video_bitmap(active_page);
          if ( active_page==page1)
     {
         active_page=page2;
     }
     else
        { active_page=page1;
        }
        
        draw_sprite(screen,score1,110,570);  
         draw_sprite(screen,lives1,580,570);
         draw_sprite(screen,hscore,340,570);

        if (timer>8 || checktimer==false)
        {
        draw_sprite(screen,enemy,ex,ey);
         draw_sprite(screen,enemy1,ex1,ey1);
         draw_sprite(screen,enemy2,ex2,ey2);
         draw_sprite(screen,enemy3,ex3,ey3);
                              
         }
        else
        {                        
         draw_sprite(screen,blue,ex,ey);
         draw_sprite(screen,blue,ex1,ey1);
         draw_sprite(screen,blue,ex2,ey2);
         draw_sprite(screen,blue,ex3,ey3);
         kill_enemy(ex,ey,x,y);
         kill_enemy1(ex1,ey1,x,y);
         kill_enemy2(ex2,ey2,x,y);
         kill_enemy3(ex3,ey3,x,y);
         }
        
 }
void loading()
{ 
      
      
      
      
    for ( counter = 0 ; counter < OBJ;counter++)
    {
     levelone[counter].disabled= false;
    }
    
    sound = load_sample("sound.wav");
    sound1 = load_sample("death.wav");
    last_page = load_bitmap("last.bmp",0); 
    scoree = load_bitmap("score1.bmp",0);
    title = load_bitmap("title.bmp",0);
    bigs = load_bmp("bs2.bmp",0);
    car1 = load_bitmap("car.bmp",0);
    car2 = load_bitmap("car2.bmp",0);
    fsprite = load_bitmap("food.bmp",0);
    enemy = load_bitmap("red.bmp",0);
    enemy1= load_bitmap("blue.bmp",0);
    enemy2= load_bitmap("orange.bmp",0);
    enemy3= load_bitmap("pink.bmp",0);
    end=load_bitmap("end.bmp",0);
    score1=load_bitmap("score.bmp",0); 
    lives1=load_bitmap("lives.bmp",0);
    name=load_bitmap("name.bmp",0);
    blank=load_bitmap("blank.bmp",0);
    won=load_bitmap("won.bmp",0);
     b_food=load_bitmap("b_food.bmp",0);
     b_food1=load_bitmap("b_food1.bmp",0);
     b_food2=load_bitmap("b_food2.bmp",0);
     b_food3=load_bitmap("b_food3.bmp",0);
     hscore=load_bitmap("hscore.bmp",0);
     blue=load_bitmap("cobra.bmp",0);
     finished=load_bitmap("finish.bmp",0);
     blank_dot=load_bitmap("blank_dot.bmp",0);
     
    page1 = create_video_bitmap(SCREEN_W, SCREEN_H);
   page2 = create_video_bitmap(SCREEN_W, SCREEN_H);
    carh=car1;    
   active_page = page2;
   
   

 }
void collision(FOOD &object)
{

if ( object.xmin1 <object.xmax2 && object.xmin2 <object.xmax1 &&
     object.ymin1 <object.ymax2 && object.ymin2 <object.ymax1  && object.disabled !=true )
     {
                  object.disabled= true ;
                   
                  score+=10;
                     finish++;
                 if(finish==255)
                  {
                                
                        stop_sample(sound);                                
                      draw_sprite(screen,finished,0,0);
                      draw_sprite (screen,scoree,0,0);
                       textprintf_ex(screen, font, 160, 40, makecol(0, 0, 0),-1, " %d", score);
                      if(score>score2)
                                      {
                                     
                                      file_handling();
                                      }
                      rest(5000);
                      exit(1);        
   //You Won............................               
                 
     }
// Score............................


     
}
}
void _Food_control()
{
     for ( counter = 0 ; counter < OBJ ; counter ++)
   { if ( levelone[counter].disabled ==false)
          draw_sprite(active_page,fsprite,levelone[counter].xmin1,levelone[counter].ymin1);
   }
   
}
void _get_cordinates()
{
     for ( counter = 0 ; counter < OBJ; counter++)
     {
      levelone[counter].xmin1=fx[counter];
      levelone[counter].ymin1=fy[counter];
      levelone[counter].xmin2=x;
      levelone[counter].ymin2=y;
      levelone[counter].xmax1=fx[counter]+fsprite->w; 
      levelone[counter].ymax1=fy[counter]+fsprite->h;

      levelone[counter].xmax2=x + carh->w ;
      levelone[counter].ymax2=y + carh->h ;
         
     }
} 
void _collision_check()
{
 for ( counter = 0 ; counter <OBJ ; counter++)
     {
               collision(levelone[counter]);
     }     
}

int test_collide_enemy(int xmin1, int ymin1)
{
    int pixel;
    pixel=getpixel(bigs,xmin1,ymin1);


    for (int i=ymin1; i<=ymin1+enemy->h; i++)
    {
        for (int j=xmin1; j<=xmin1+enemy->w; j++)
        {
            pixel=getpixel(bigs,j,i);
            if ( pixel == makecol(0,0,255))
            {  
               
                 return 1;
                  
                 break;
                  
                               
            }

        }
    }
    return 0;
}
//---------------------------
int test_collide_enemy1(int xmin1, int ymin1)
{
    int pixel;
    pixel=getpixel(bigs,xmin1,ymin1);


    for (int i=ymin1; i<=ymin1+enemy1->h; i++)
    {
        for (int j=xmin1; j<=xmin1+enemy1->w; j++)
        {
            pixel=getpixel(bigs,j,i);
            if ( pixel == makecol(0,0,255))
            {  
                
                 return 1;
                
                 break;
                  
                               
            }

        }
    }
    return 0;
}

//-----------------------------------------
int test_collide_enemy2(int xmin1, int ymin1)
{
    int pixel;
    pixel=getpixel(bigs,xmin1,ymin1);


    for (int i=ymin1; i<=ymin1+enemy2->h; i++)
    {
        for (int j=xmin1; j<=xmin1+enemy2->w; j++)
        {
            pixel=getpixel(bigs,j,i);
            if ( pixel == makecol(0,0,255))
            {  
              
                 return 1;
                  
                               
                 break;
                   
            }

        }
    }
    return 0;
    
}
//--------------------------------------------
int test_collide_enemy3(int xmin1, int ymin1)
{
    int pixel;
    pixel=getpixel(bigs,xmin1,ymin1);


    for (int i=ymin1; i<=ymin1+enemy3->h; i++)
    {
        for (int j=xmin1; j<=xmin1+enemy3->w; j++)
        {
            pixel=getpixel(bigs,j,i);
            if ( pixel == makecol(0,0,255))
            {  
                
                 return 1;
                 
                 break;
                               
            }

        }
    }
    return 0;
}
//------------------------------------------------------------
int test_collide_carh(int xmin1, int ymin1)
{
    int pixel;
    pixel=getpixel(bigs,xmin1,ymin1);


    for (int i=ymin1; i<=ymin1+carh->h; i++)
    {
        for (int j=xmin1; j<=xmin1+carh->w; j++)
        {
            pixel=getpixel(bigs,j,i);
            if ( pixel == makecol(0,0,255))
            {  
                 
                return 1;
                break;
            }

        }
    }
    return 0;
}
int death_collide(int exx,int eyy,int px, int py)
{
  
 
 if((px>=exx && px<=exx+enemy->w ) && (py>=eyy && py<=eyy+enemy->h) ||
 (px+carh->w>=exx && px+carh->w<=exx+enemy->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy->h)||
 (px+carh->w>=exx && px+carh->w<=exx+enemy->w ) && (py>=eyy && py<=eyy+enemy->h)||
 (px>=exx && px<=exx+enemy->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy->h))
 {
           play_sample(sound, 0, 128,1000, 0);
           play_sample(sound1, 255, 128,2000, 0);
           lives--;
           if(lives==0)
           {           
                      stop_sample(sound); 
                     // play_sample(sound, 0, 128,1000, 1);
                            draw_sprite(screen,end,260,160);
                                       rest(3000);
                                         stop_sample(sound); 
                                        // play_sample(sound, 0, 128,1000, 1);
                       draw_sprite(screen,blank,0,0);
                       draw_sprite(screen,last_page,0,80);
                       draw_sprite(screen,scoree,150,550);
                       textprintf_ex(screen, font, 300, 570, makecol(255, 255, 255),-1, " %d", score);
                                       rest(2000);
                                      if(score>score2)
                                      {
                                     //  textprintf_ex(screen, font, 100, 100, makecol(255, 255, 255),-1, "File Handling");
                                      file_handling(); 
                                       exit(0);
                                      }
                                       exit(0);
                                       
                                       
                       }
                  else
                  {             
                   rest(500);
                   x=395;
                   y=420;
                   ex=700;
                   ey=40;
                   ex1=50;
                   ey1=50;
                   ex2=50;
                   ey2=515;
                   ex3=700;
                   ey3=520;
                   rest(1000);
                   }
           }         
}
void bonus_food(int exx,int eyy,int px, int py)
{ 
for(int i= px;i<=px+carh->w;i++)
{
        for(int j=py;j<=py+carh->h;j++)
            {
                if((i>=exx && i<= exx+b_food->w) && (j>=eyy && j<=eyy+b_food->h))
                   {
                           //top left
        if(x<(screen->w)/2 && y<(screen->h)/2 && flage==0)
        {clear(b_food2);
          score+=30;
          flage++;
           timer=0;
                   if(timer<=8)
         {
          checktimer=true;                           
         }
         else
         {
             
             checktimer=false;    
         }
        
         }
        //top right
        if(x>(screen->w)/2 && y<(screen->h)/2 && flage1==0)
        { 
          clear(b_food3);
          score+=30;
          flage1++; 
           timer=0;
                   if(timer<=8)
         {
          checktimer=true;                           
         }
         else
         {
             
             checktimer=false;    
         }
        }
        //bottom left
        if(x<(screen->w)/2 && y>(screen->h)/2 && flage2==0)
        {
          clear(b_food);
          score+=30;
          flage2++;
           timer=0;
                   if(timer<=8)
         {
          checktimer=true;                           
         }
         else
         {
             
             checktimer=false;    
         }
        }
        //bottom right
        if(x>(screen->w)/2 && y>(screen->h)/2 && flage3==0)
        {
          clear(b_food1);
          score+=30;
          flage3++; 
           timer=0;
                   if(timer<=8)
         {
          checktimer=true;                           
         }
         else
         {
             
             checktimer=false;    
         }
        }
                           }
                           }
                }
        }     
         
         
              
void kill_enemy(int exx,int eyy,int px, int py)
{
     if((px>=exx && px<=exx+enemy->w ) && (py>=eyy && py<=eyy+enemy->h) ||
 (px+carh->w>=exx && px+carh->w<=exx+enemy->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy->h)||
 (px+carh->w>=exx && px+carh->w<=exx+enemy->w ) && (py>=eyy && py<=eyy+enemy->h)||
 (px>=exx && px<=exx+enemy->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy->h))
          {
                               play_sample(sound, 0, 128,1000, 0);
           play_sample(sound1, 255, 128,2000, 0);
              rest(100);
              ex=380;
              ey=250;
              edx=0;
              edy=-7;
                              }

     }
void kill_enemy1(int exx,int eyy,int px, int py)
{
     if((px>=exx && px<=exx+enemy1->w ) && (py>=eyy && py<=eyy+enemy1->h) ||
 (px+carh->w>=exx && px+carh->w<=exx+enemy1->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy1->h)||
 (px+carh->w>=exx && px+carh->w<=exx+enemy1->w ) && (py>=eyy && py<=eyy+enemy1->h)||
 (px>=exx && px<=exx+enemy1->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy1->h))
          {
                                play_sample(sound, 0, 128,1000, 0);
           play_sample(sound1, 255, 128,2000, 0);
              rest(100);
              ex1=380;
              ey1=250;
              edx1=0;
              edy1=-7;
              
                              }

     }

void kill_enemy2(int exx,int eyy,int px, int py)
{
      if((px>=exx && px<=exx+enemy2->w ) && (py>=eyy && py<=eyy+enemy2->h) ||
 (px+carh->w>=exx && px+carh->w<=exx+enemy2->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy2->h)||
 (px+carh->w>=exx && px+carh->w<=exx+enemy2->w ) && (py>=eyy && py<=eyy+enemy2->h)||
 (px>=exx && px<=exx+enemy2->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy2->h))
          {
                                play_sample(sound, 0, 128,1000, 0);
           play_sample(sound1, 255, 128,2000, 0);
              rest(100);
              ex2=380;
              ey2=250;
              edx2=0;
              edy2=-7;
              
              
                              }

     }
void kill_enemy3(int exx,int eyy,int px, int py)
{
      if((px>=exx && px<=exx+enemy3->w ) && (py>=eyy && py<=eyy+enemy3->h) ||
 (px+carh->w>=exx && px+carh->w<=exx+enemy3->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy3->h)||
 (px+carh->w>=exx && px+carh->w<=exx+enemy3->w ) && (py>=eyy && py<=eyy+enemy3->h)||
 (px>=exx && px<=exx+enemy3->w ) && (py+carh->h>=eyy && py+carh->h<=eyy+enemy3->h))
          {
                               play_sample(sound, 0, 128,1000, 0);
              rest(100);
              ex3=380;
              ey3=250;
              edx3=0;
              edy3=-7;
              
                            }

     }
void file_handling(void)
{

FILE *out;

      out=fopen("score.txt","w");
      fprintf(out,"%s %d",pname,score);
      
      fclose(out);    
                   
     }
void sorting(void)
{
     int max=0;
     int c=0;
     char pname2;
     
     FILE *inn;
     inn=fopen("score.txt","r");
     while (fscanf(inn,"%s %d",&pname2,&score2)!=EOF);
     {   
         }
    
     textprintf_ex(screen, font, 435, 585, makecol(255, 255, 255),-1, " %d", score2);
      fclose(inn);    
}    
