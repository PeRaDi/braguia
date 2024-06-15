type ContactCardProps = {
  recordId: string;
  name: string;
  number: string;
  avatar: string;
  isSwitchOn: boolean;
  onToggleSwitch: (
    recordId: string,
    isOn: boolean,
    setIsOn: React.Dispatch<React.SetStateAction<boolean>>,
  ) => void; // Handler for toggling the switch
};

export default ContactCardProps;
