library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity nexys4_fifo8x8 is
    Port (  clk : in STD_LOGIC;
            btn : in STD_LOGIC_VECTOR (4 downto 0);
            sw : in STD_LOGIC_VECTOR (15 downto 0);
            led : out STD_LOGIC_VECTOR (15 downto 0);
            an : out STD_LOGIC_VECTOR (7 downto 0);
            cat : out STD_LOGIC_VECTOR (6 downto 0));
end nexys4_fifo8x8;

architecture Behavioral of nexys4_fifo8x8 is

component debouncer is
  Port ( clk : in std_logic;
        btn : in std_logic;
        en : out std_logic );
end component;

component fifo_ctrl is
    Port ( rd : in STD_LOGIC;
           wr : in STD_LOGIC;
           clk : in STD_LOGIC;
           rst : in STD_LOGIC;
           rd_inc : out STD_LOGIC;
           wr_inc : out STD_LOGIC;
           full : out STD_LOGIC;
           empty : out STD_LOGIC);
end component;

component fifo_8bit is
    Port ( rd : in STD_LOGIC;
           data_in : in STD_LOGIC_VECTOR (7 downto 0);
           wr : in STD_LOGIC;
           wr_inc : in STD_LOGIC;
           rd_inc : in STD_LOGIC;
           rst : in STD_LOGIC;
           clk : in STD_LOGIC;
           data_out : out STD_LOGIC_VECTOR (7 downto 0));
end component;

component display_7seg is
    Port ( digit0 : in STD_LOGIC_VECTOR (3 downto 0);
           digit1 : in STD_LOGIC_VECTOR (3 downto 0);
           digit2 : in STD_LOGIC_VECTOR (3 downto 0);
           digit3 : in STD_LOGIC_VECTOR (3 downto 0);
           clk : in STD_LOGIC;
           cat : out STD_LOGIC_VECTOR (6 downto 0);
           an : out STD_LOGIC_VECTOR (3 downto 0));
end component;

signal empty, full, wr_btn, rd_btn, rst_btn, rd_inc, wr_inc : std_logic := '0';
signal data_in, data_out : std_logic_vector (7 downto 0) := (others => '0');


begin
    write_button : debouncer port map(clk => clk, btn => btn(0), en => wr_btn);
    read_button : debouncer port map(clk => clk, btn => btn(1), en => rd_btn);
    
    fifo_control : fifo_ctrl port map(
        rd => rd_btn,
        wr => wr_btn,
        clk => clk,
        rst => rst_btn,
        rd_inc => rd_inc,
        wr_inc => wr_inc,
        full => full,
        empty => empty
    );

    fifo : fifo_8bit port map(
        data_in => data_in,
        wr => wr_btn,
        rd => rd_btn,
        rd_inc => rd_inc,
        wr_inc => wr_inc,
        clk => clk,
        rst => rst_btn,
        data_out => data_out
    );

    ssd : display_7seg port map(
        clk => clk,
        digit3 => data_in(7 downto 4),
        digit2 => data_in(3 downto 0),
        digit1 => data_out(7 downto 4),
        digit0 => data_out(3 downto 0),
        cat => cat(6 downto 0),
        an => an(3 downto 0)
    );

    data_in <= sw(15 downto 8);
    rst_btn <= btn(2);
    
    
end Behavioral;
