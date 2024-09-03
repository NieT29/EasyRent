package com.example.easyrent.controller.view;

import com.example.easyrent.entity.Deposit;
import com.example.easyrent.entity.User;
import com.example.easyrent.model.enums.DepositStatus;
import com.example.easyrent.model.enums.SubjectRent;
import com.example.easyrent.repository.DepositRepository;
import com.example.easyrent.service.CategoryService;
import com.example.easyrent.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quan-ly")
public class UserController {
    private final DepositRepository depositRepository;
    private final DepositService depositService;
    private final CategoryService categoryService;

    @GetMapping
    public String getHome() {
        return "user/pages/index";
    }

    @GetMapping("/thong-tin-ca-nhan")
    public String getUserInfor() {
        return "user/pages/user-infor";
    }

    @GetMapping("/doi-mat-khau")
    public String changePassword() {
        return "user/pages/change-password";
    }

    @GetMapping("/nap-tien")
    public String deposit() {
        return "user/pages/deposit";
    }

    @GetMapping("/nap-tien/vnpay")
    public String vnpay() {
        return "user/pages/vnpay";
    }

    @GetMapping("/nap-tien/xu-ly")
    public String handlePaymentReturn(
            @RequestParam("vnp_Amount") String amount,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TxnRef") String txnRef,
            @RequestParam("vnp_TransactionStatus") String transactionStatus,
            @RequestParam("vnp_TransactionNo") String transactionNo,
            Model model) {

        // Tìm giao dịch nạp tiền trong cơ sở dữ liệu dựa trên txnRef (mã giao dịch)
        Deposit deposit = depositRepository.findByVnpTransactionId(txnRef).orElseThrow(
                () -> new RuntimeException("Transaction not found")
        );

        boolean isSuccess = "00".equals(responseCode) && "00".equals(transactionStatus);

        if (isSuccess) {
            // Giao dịch thành công
            deposit.setStatus(DepositStatus.COMPLETED);
            deposit.setVnpTransactionId(txnRef);
            // Cập nhật số dư cho người dùng
            User user = deposit.getUser();
            user.setAccountBalance(user.getAccountBalance() + deposit.getTotalAmount());
            depositRepository.save(deposit);
        } else {
            // Giao dịch thất bại
            deposit.setStatus(DepositStatus.FAILED);
            depositRepository.save(deposit);
        }

        return "redirect:/quan-ly/nap-tien/ket-qua?id=" + txnRef;
    }

    @GetMapping("/nap-tien/ket-qua")
    public String showPaymentResult(@RequestParam("id") String txnRef, Model model) {
        // Tìm giao dịch nạp tiền trong cơ sở dữ liệu dựa trên txnRef
        Deposit deposit = depositRepository.findByVnpTransactionId(txnRef).orElseThrow(
                () -> new RuntimeException("Transaction not found")
        );

        // Kiểm tra xem giao dịch đã hoàn tất hay thất bại
        boolean isSuccess = deposit.getStatus() == DepositStatus.COMPLETED;

        // Truyền thông tin vào model để hiển thị trên trang kết quả
        model.addAttribute("isSuccess", isSuccess);
        model.addAttribute("deposit", deposit);
        model.addAttribute("transactionStatus", isSuccess ? "Thành công" : "Thất bại");

        return "user/pages/payment-result";
    }

    @GetMapping("/lich-su-nap-tien")
    public String depositHistory(Model model) {
        List<Deposit> deposits = depositService.getDepositHistory();

        model.addAttribute("deposits", deposits);
        return "user/pages/deposit-history";
    }


    @GetMapping("/dang-tin-moi")
    public String createPost(Model model) {
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("subjectRent", SubjectRent.values());
        return "user/pages/create-post";
    }
}
