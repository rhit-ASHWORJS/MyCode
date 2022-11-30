from re import L
import random


board = [['b1','b2','b3'],['  ','  ','  '],['  ','  ','  '],['  ','  ','  '],['  ','  ','  '],['w1','w2','w3']]

def posInBoard(board, i, j):
    if(0<= i and i<len(board)):
        if(0<=j and j<len(board[0])):
            return True
    return False

def getValidMoveDict(board, color):
    validMoves = {}
    validTakes = {}
    take = False

    for i in range(1,4):
        validMoves.update({color + str(i): []})
        validTakes.update({color + str(i): []})

    for i in range(len(board)):
        for j in range(len(board[0])):
            if(board[i][j]!="  " and board[i][j][0]==color):
                #Check all positions near it
                for imod in [-1,0,1]:
                    for jmod in [-1,0,1]:       
                        if(posInBoard(board,i+imod,j+jmod) and not(imod==0 and jmod==0)):
                            if(board[i+imod][j+jmod]=="  "):
                                validMoves.get(board[i][j]).append([i+imod, j+jmod])
                            elif(board[i+imod][j+jmod][0] != color):
                                validTakes.get(board[i][j]).append([i+imod, j+jmod])
                                take = True

    if(take):
        return validTakes
    else:
        return validMoves
    
def getValidMoveList(board, color):
    list = []
    dict = getValidMoveDict(board, color)
    for key in dict:
        for value in dict.get(key):
            list.append(key + " " + str(value[0]) + " " + str(value[1]))
    return list

def getPositionOfPiece(board, piece):
    for i in range(len(board)):
        for j in range(len(board[0])):
            if(board[i][j]==piece):
                return [i, j]

def makeMoveOnBoard(board, piece, i, j):
    moveList = getValidMoveList(board, piece[0])
    if(not (piece + " " + str(i) + " " + str(j)) in moveList):
        print("Invalid move:" + piece + " " + str(i) + " " + str(j))
        return []
    position = getPositionOfPiece(board, piece)
    board[int(i)][int(j)] = piece
    board[position[0]][position[1]] = "  "
    return board
    
def getRandomMove(board, color):
    list = getValidMoveList(board, color)
    if(len(list)==0):
        print("No possible moves.  Did I win???")
        return " "
    return random.choice(list)

def playGameRandomB():
    for row in board:
            print(row)
    print("moves for w: " + str(getValidMoveList(board, "w")))
    # print("moves for b: " + str(getValidMoveList(board, "b")))
    while(True):
        move = input("Make a move\n")
        moveList = move.split(" ")
        makeMoveOnBoard(board, moveList[0], moveList[1], moveList[2])
        # for row in board:
        #     print(row)
        # print("moves for w: " + str(getValidMoveList(board, "w")))
        # print("moves for b: " + str(getValidMoveList(board, "b")))
        # print("------------------")
        if(len(getValidMoveList(board, "b"))==0):
            print("Oh no!  You lost!")
            return False
        otherMove = getRandomMove(board, "b")
        moveList = otherMove.split(" ")
        makeMoveOnBoard(board, moveList[0], moveList[1], moveList[2])
        for row in board:
            print(row)
        if(len(getValidMoveList(board, "w"))==0):
            print("Awesome!  You Win!")
            return True
        print("moves for w: " + str(getValidMoveList(board, "w")))

def playGameRandomW():
    while(True):
        otherMove = getRandomMove(board, "w")
        moveList = otherMove.split(" ")
        makeMoveOnBoard(board, moveList[0], moveList[1], moveList[2])
        for row in board:
            print(row)
        if(len(getValidMoveList(board, "b"))==0):
            print("Awesome!  You Win!")
            return True
        print("moves for b: " + str(getValidMoveList(board, "b")))
        move = input("Make a move\n")
        moveList = move.split(" ")
        makeMoveOnBoard(board, moveList[0], moveList[1], moveList[2])
        # for row in board:
        #     print(row)
        # print("moves for w: " + str(getValidMoveList(board, "w")))
        # print("moves for b: " + str(getValidMoveList(board, "b")))
        # print("------------------")
        if(len(getValidMoveList(board, "w"))==0):
            print("Oh no!  You lost!")
            return False

playGameRandomW()