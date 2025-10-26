export default interface ProfileProps {
    isDialogVisible: boolean;
    setIsDialogVisible: (visible: boolean) => void;
    handleLogout: () => void;
}