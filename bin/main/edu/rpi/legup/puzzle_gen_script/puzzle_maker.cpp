//
//  puzzle_maker.cpp
//  
//
//  Created by Chris Vanderloo on 4/15/18.
//
#include <stdio.h>
#include <iostream>
#include <unistd.h>
#include <sys/ioctl.h>
#include <algorithm>
#include <vector>
#include <string>
#include <sys/stat.h>
#include <fstream>

void ansi_setup() {
    //-- SET UP INTERFACE
    struct winsize w;
    int i;
    ioctl(STDOUT_FILENO,TIOCGWINSZ,&w);
    
    for (i=0; i<w.ws_row; i++) {
        for (int j=0; j<w.ws_col; j++) {
            printf(" ");
        }
        printf("\n");
    }
    printf("\e[%dA", w.ws_row);
    
    for (i=0; i<w.ws_col; i++) {
        printf("_");
    }
    for (i=0; i<w.ws_row-3; i++) {
        printf(" \n");
    }
    for (i=0; i<w.ws_col; i++) {
        printf("_");
    }
    printf("\n");
    printf("\e[%dA", w.ws_row-3);
    //-- END SET UP INTERFACE
}

bool fileExists(const std::string& filename)
{
    struct stat buf;
    if (stat(filename.c_str(), &buf) != -1)
    {
        return true;
    }
    return false;
}

int main() {
    ansi_setup();
    
    printf("CURRENT PUZZLES AVAILABLE\n|\n");
    FILE *fp;
    char path[PATH_MAX];
    fp = popen("cd ../puzzlefiles && ls", "r");
    std::vector<std::string> puzzles;
    while (fgets(path, PATH_MAX, fp) != NULL) {
        std::string str = path;
        str.erase(remove_if(str.begin(), str.end(), isspace), str.end());
        puzzles.push_back(str);
        printf("|-- %s", path);
    }
    printf("\n\n");
    std::string name;
    std::string res;
    while (true) {
        printf("\e[KPuzzle Qualified Class Name (with correct capitaliztion): ");
        name = "";
        std::cin >> name;
        res = name;
        std::transform(name.begin(), name.end(), name.begin(), ::tolower);
        if (std::find(puzzles.begin(), puzzles.end(), name) != puzzles.end()) break;
        else printf("\e[A\e[K\e[A\e[K\e[1mInvalid Selection. \e[0mTry again.\n" );
    }
    ansi_setup();
    
    int n=1;
    printf("\e[3;H\e[1m%s\e[0m\n\n", res.c_str());
    while (fileExists(res+"Board"+std::to_string(n))){
        n++;
    }
    std::ofstream puzzle(res+"Board"+std::to_string(n));
    if (puzzle) std::cout << "CREATED FILE - /"+res+"Board"+std::to_string(n)+"\n\n";
    else {
        std::cout << "FAILED TO OPEN FILE.\n\n";
        return 0;
    }
    printf("Board Width: ");
    int width,height;
    scanf("%d", &width);
    printf("\e[A\e[KBoard Height: ");
    scanf("%d", &height);
    
    std::string output = "";
    output = output +
    "<Legup>\n" +
    "    <puzzle name=\"" + res + "\">\n" +
    "        <board width=\"" + std::to_string(width) + "\" height=\"" + std::to_string(height) + "\">\n" +
    "            <cells>\n";
    std::cout << "\e[A\e[K\e[36m" << output << "\e[0m";
    puzzle << output;
    
    while (true) {
        int x,y,val;
        std::cout << "\e[1mADD CELL (type -1 at any point to finish)\nX: \e[0m";
        std::cin >> x;
        if(x==-1) {
            printf("\e[K\e[A\e[K");
            break;
        }
        std::cout << "\e[1mY: \e[0m";
        std::cin >> y;
        if(y==-1) {
            printf("\e[K\e[A\e[K\e[A\e[K");
            break;
        }
        std::cout << "\e[1mVal: \e[0m";
        std::cin >> val;
        if(val==-1) {
            printf("\e[K\e[A\e[K\e[A\e[K\e[A\e[K");
            break;
        }
        std::cout << "\e[A\e[K\e[A\e[K\e[A\e[K\e[A\e[K\n";
        output = "                <cell value=\"" + std::to_string(val) + "\" x=\"" + std::to_string(x) + "\" y=\"" + std::to_string(y) + "\"/>\n";
        std::cout << "\e[A\e[K\e[36m" << output << "\e[0m";
        puzzle << output;
    }
    output = "            </cells>\n        </board>\n    </puzzle>\n</Legup>";
    std::cout << "\e[A\e[K\e[36m" << output << "\e[0m";
    puzzle << output;
    
    
    
    puzzle.close();
}
