N = 1000;
T = 0:300;

X = 500;
A = floor(X * 0.5);
B = X - A;

Y = N - X;
C = floor(Y * 0.5);
D = Y - C;

W = (100/N)*((7*A+5*B)*(18-log2(T+1))+(1*C+3*D)*log2(T+2));

if W(1,1) >= 8500
    P = 'First';
elseif W(1,1) >= 6100 && W(1,1) < 8500
    P = 'Second';
elseif W(1,1) >=3800 && W(1,1) < 6100
    P = 'Third';
elseif W(1,1) >= 1500 && W(1,1) < 3800
    P = 'Forth';
elseif W(1,1) < 1500
    P = 'Fifth';
end

plot(T, W, 'r');  % Plot W
hold on;
grid;